import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { iTask, iTaskCreate } from '../../task.model';
import { TaskFormDialog } from '../../components/task-form-dialog/task-form-dialog';
import { TaskColumn } from '../../components/task-column/task-column';
import { TaskService } from '../../task.service';

type DialogState =
  | { open: false }
  | { open: true; mode: 'create'; presetDone: boolean }
  | { open: true; mode: 'edit'; initial: iTask };

@Component({
  selector: 'app-tasks-board',
  standalone: true,
  imports: [CommonModule, TaskColumn, TaskFormDialog],
  templateUrl: './tasks-board.html',
  styleUrl: './tasks-board.css',
})
export class TasksBoard implements OnInit {
  tasks: iTask[] = [];
  loading = false;
  serverErrors: { field: string; message: string }[] | null = null;

  dialog: DialogState = { open: false };
  private api = inject(TaskService);

  ngOnInit(): void {
    this.refresh();
  }

  get todo(): iTask[] {
    return this.tasks.filter((t) => !t.done);
  }
  get done(): iTask[] {
    return this.tasks.filter((t) => t.done);
  }

  refresh(): void {
    this.loading = true;
    this.api.list().subscribe({
      next: (ts) => {
        this.tasks = ts;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      },
    });
  }

  onAddRequested(presetDone: boolean) {
    this.serverErrors = null;
    this.dialog = { open: true, mode: 'create', presetDone };
  }

  onEditRequested(task: iTask) {
    this.serverErrors = null;
    this.dialog = { open: true, mode: 'edit', initial: task };
  }

  onToggle(task: iTask) {
    const updated: iTask = { ...task, done: !task.done };
    this.api.update(updated).subscribe({
      next: (t2) => {
        this.tasks = this.tasks.map((x) => (x.id === t2.id ? t2 : x));
      },
      error: (err) => console.error(err),
    });
  }

  onDelete(task: iTask) {
    if (!confirm('Supprimer cette tâche ?')) return;
    this.api.delete(task.id).subscribe({
      next: () => {
        this.tasks = this.tasks.filter((x) => x.id !== task.id);
      },
      error: (err) => console.error(err),
    });
  }

  onDialogCancel() {
    this.serverErrors = null;
    this.dialog = { open: false };
  }

  onDialogSubmit(data: iTaskCreate & { id?: number }) {
    if (data.id) {
      // Edit existing task
      const payload: iTask = {
        id: data.id,
        title: data.title,
        description: data.description ?? null,
        dueDate: data.dueDate ?? null,
        done: !!data.done,
      };
      this.api.update(payload).subscribe({
        next: (t2) => {
          this.tasks = this.tasks.map((x) => (x.id === t2.id ? t2 : x));
          this.onDialogCancel();
        },
        error: (err) => this.handleApiError(err),
      });
    } else {
      // Create new task
      this.api
        .create({
          title: data.title,
          description: data.description ?? null,
          dueDate: data.dueDate ?? null,
          done: !!data.done,
        })
        .subscribe({
          next: (created) => {
            // on ajoute en tête pour le feedback immédiat
            this.tasks = [created, ...this.tasks];
            this.onDialogCancel();
          },
          error: (err) => this.handleApiError(err),
        });
    }
  }

  private handleApiError(err: any) {
    const api = err?.error;
    if (err?.status === 422 && api?.type === 'validation_error') {
      // Normalise les clés de champs et garde la modale ouverte
      this.serverErrors = (api.errors ?? []).map((e: any) => ({
        field: this.pickField(e.field),
        message: e.message ?? 'Invalid field',
      }));
      return; // ne ferme pas la modale
    } else if (err?.status === 400 && api?.type === 'database_error') {
      alert('Server database error: ' + api?.message);
      return;
    }
    alert(api?.message ?? 'Erreur serveur');
  }

  // 'create.arg0.title' -> 'title', 'update.arg0.dueDate' -> 'dueDate'
  private pickField(f?: string): 'title' | 'description' | 'dueDate' | 'done' | '' {
    if (!f) return '';
    const last = f.split('.').pop() || '';
    const key = last.toLowerCase();
    if (key === 'title') return 'title';
    if (key === 'description') return 'description';
    if (key === 'duedate' || last === 'dueDate') return 'dueDate';
    if (key === 'done') return 'done';
    return '';
  }
}
