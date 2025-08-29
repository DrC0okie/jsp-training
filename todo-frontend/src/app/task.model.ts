export interface iTask {
  id: number;
  title: string;
  description?: string | null;
  done: boolean;
  dueDate?: string | null;
}

export interface iTaskCreate {
  title: string;
  description?: string | null;
  dueDate?: string | null;
  done?: boolean;
}
