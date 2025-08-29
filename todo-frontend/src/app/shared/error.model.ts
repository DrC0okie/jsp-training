export interface ApiErrorField {
  field: string;
  message: string;
}
export interface ApiError {
  type: string;
  message: string;
  errors?: ApiErrorField[];
}
