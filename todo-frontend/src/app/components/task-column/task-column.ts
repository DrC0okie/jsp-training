import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { iTask } from '../../task.model';
import { TaskCard } from '../task-card/task-card';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-task-column',
  standalone: true,
  imports: [CommonModule, TaskCard, MatIconModule],
  templateUrl: './task-column.html',
  styleUrl: './task-column.css'
})
export class TaskColumn {
  @Input({ required: true }) title!: string;
  @Input({ required: true }) tasks: iTask[] = [];

  @Output() addRequested = new EventEmitter<void>();
  @Output() toggle = new EventEmitter<iTask>();
  @Output() editRequested = new EventEmitter<iTask>();
  @Output() deleteRequested = new EventEmitter<iTask>();
}
