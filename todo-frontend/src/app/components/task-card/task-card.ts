import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { iTask } from '../../task.model';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-task-card',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './task-card.html',
  styleUrl: './task-card.css',
})
export class TaskCard {
  @Input({ required: true }) task!: iTask;

  @Output() toggle = new EventEmitter<iTask>();
  @Output() edit = new EventEmitter<iTask>();
  @Output() delete = new EventEmitter<iTask>();

  private todayIso = new Date().toISOString().slice(0, 10);

  isOverdue(t: iTask): boolean {
    return !!t.dueDate && t.dueDate < this.todayIso && !t.done;
  }
  isDueSoon(t: iTask): boolean {
    if (!t.dueDate || t.done) return false;
    const ms = Date.parse(t.dueDate) - Date.now();
    const days = Math.floor(ms / 86_400_000);
    return days >= 0 && days <= 3;
  }
  isDueLater(t: iTask){
  if(!t.dueDate || t.done) return false;
  const days = Math.floor((Date.parse(t.dueDate)-Date.now())/86_400_000);
  return days > 3;
}
}
