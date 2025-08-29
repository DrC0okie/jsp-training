import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnChanges,
  SimpleChanges,
  Output,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  AbstractControl,
} from '@angular/forms';
import { iTask, iTaskCreate } from '../../task.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  selector: 'app-task-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './task-form-dialog.html',
  styleUrl: './task-form-dialog.css',
})
export class TaskFormDialog implements OnChanges {
  @Input() serverErrors: { field: string; message: string }[] | null = null;
  @Input({ required: true }) mode: 'create' | 'edit' = 'create';
  @Input() initial: iTask | null = null;
  @Input() presetDone: boolean | null = null;

  @Output() submitted = new EventEmitter<iTaskCreate & { id?: number }>();
  @Output() cancelled = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  submittedOnce = false;

  form = this.fb.group({
    title: this.fb.control<string>('', [
      Validators.required,
      Validators.minLength(1),
      Validators.maxLength(100),
    ]),
    description: this.fb.control<string>('', [Validators.maxLength(300)]),
    dueDate: this.fb.control<Date | null>(null),
    done: this.fb.control<boolean>(false),
  });

  ngOnChanges(changes: SimpleChanges): void {
    // 1) réinit du formulaire quand mode/initial/presetDone change
    if (changes['mode'] || changes['initial'] || changes['presetDone']) {
      if (this.mode === 'edit' && this.initial) {
        this.form.reset(
          {
            title: this.initial.title ?? '',
            description: this.initial.description ?? '',
            dueDate: this.isoToDate(this.initial.dueDate), // ISO -> Date
            done: this.initial.done,
          },
          { emitEvent: false }
        );
      } else {
        this.form.reset(
          {
            title: '',
            description: '',
            dueDate: null, // Datepicker => null
            done: this.presetDone ?? false,
          },
          { emitEvent: false }
        );
      }
      this.form.markAsPristine();
      this.form.markAsUntouched();
    }

    // réappliquer les erreurs serveur quand elles changent
    if (changes['serverErrors']) {
      // retirer d’abord les erreurs 'backend' existantes
      for (const c of Object.values(this.form.controls)) {
        const ctl = c as AbstractControl;
        if (ctl.hasError('backend')) {
          const { backend, ...rest } = ctl.errors ?? {};
          ctl.setErrors(Object.keys(rest).length ? rest : null, { emitEvent: false });
        }
      }
      // poser les nouvelles erreurs backend
      if (this.serverErrors?.length) {
        for (const e of this.serverErrors) {
          const key = this.mapField(e.field);
          if (!key) continue;
          const ctl = this.form.get(key)!;
          ctl.setErrors(
            { ...(ctl.errors ?? {}), backend: e.message || true },
            { emitEvent: false }
          );
        }
        this.form.markAllAsTouched();
      }
    }
  }

  submit(): void {
    this.submittedOnce = true;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      (document.querySelector('.dialog form .ng-invalid') as HTMLElement)?.focus();
      return;
    }
    const v = this.form.value;
    const dueDate = v.dueDate ? this.dateToIso(v.dueDate as Date) : null;

    const payload = {
      title: (v.title ?? '').toString(),
      description: (v.description ?? '') || null,
      dueDate,
      done: !!v.done,
      ...(this.mode === 'edit' && this.initial ? { id: this.initial.id } : {}),
    };
    this.submitted.emit(payload);
  }

  // Helpers erreurs front
  titleError(): string | null {
    const c = this.form.controls.title;
    if (!c || !(c.touched || this.submittedOnce)) return null;
    if (c.hasError('required')) return 'Le titre est requis';
    if (c.hasError('minlength')) return 'Au moins 2 caractères';
    if (c.hasError('maxlength')) return 'Maximum 100 caractères';
    return null;
  }
  descError(): string | null {
    const c = this.form.controls.description;
    if (!c || !(c.touched || this.submittedOnce)) return null;
    if (c.hasError('maxlength')) return 'Maximum 300 caractères';
    return null;
  }

  // Erreur serveur pour un champ (422)
  serverErrorFor(field: 'title' | 'description' | 'dueDate'): string | null {
    if (!this.serverErrors?.length) return null;
    const m = this.serverErrors.find((e) => e.field?.includes(field));
    return m?.message ?? null;
  }

  // Quand l’utilisateur retouche un champ → on efface l’erreur serveur de ce champ
  clearServerError(field: 'title' | 'description' | 'dueDate') {
    const ctl = this.form.get(field);
    if (ctl?.hasError('backend')) {
      const { backend, ...rest } = ctl.errors ?? {};
      ctl.setErrors(Object.keys(rest).length ? rest : null);
    }
    if (this.serverErrors) {
      this.serverErrors = this.serverErrors.filter((e) => !e.field?.toLowerCase().includes(field));
    }
  }

  private isoToDate(iso?: string | null): Date | null {
    if (!iso) return null;
    // évite les décalages de fuseau
    const [y, m, d] = iso.split('-').map(Number);
    return new Date(y, (m ?? 1) - 1, d ?? 1);
  }
  private dateToIso(d?: Date | null): string | null {
    if (!d) return null;
    const pad = (n: number) => (n < 10 ? '0' + n : '' + n);
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
  }

  // Mappe un nom de champ côté API -> clé du FormGroup
  private mapField(f?: string): 'title' | 'description' | 'dueDate' | null {
    if (!f) return null;
    const s = f.toLowerCase();
    if (s.includes('title')) return 'title';
    if (s.includes('description')) return 'description';
    if (s.includes('duedate') || s.includes('due_date') || s === 'dueDate') return 'dueDate';
    return null;
  }
}
