import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { iTask, iTaskCreate } from './task.model';

@Injectable({ providedIn: 'root' })
export class TaskService {
  private readonly base = '/api/v1/tasks';

  constructor(private http: HttpClient) {}

  list(): Observable<iTask[]> {
    return this.http.get<iTask[]>(this.base);
  }

  create(input: iTaskCreate): Observable<iTask> {
    return this.http.post<iTask>(this.base, input);
  }

  get(id: number): Observable<iTask> {
    return this.http.get<iTask>(`${this.base}/${id}`);
  }

  update(t: iTask): Observable<iTask> {
    const body = {
      title: t.title,
      description: t.description ?? null,
      dueDate: t.dueDate ?? null,
      done: t.done
    };
    return this.http.put<iTask>(`${this.base}/${t.id}`, body);
  }

  delete(id: number): Observable<void> {
    return this.http.delete(`${this.base}/${id}`).pipe(map(() => void 0));
  }
}
