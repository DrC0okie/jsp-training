import { Component } from '@angular/core';
import { TasksBoard } from './containers/tasks-board/tasks-board';
import localeCh from '@angular/common/locales/fr-CH';
import { registerLocaleData } from '@angular/common';

registerLocaleData(localeCh);

@Component({
  selector: 'app-root',
  imports: [TasksBoard],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

}
