package com.tiass.services.data.served;

import org.springframework.http.ResponseEntity;

public class TiassResponse<T> {
	private T tiassResults;
	private ValidationError errors;
	private boolean success;

	public T getTiassResults() {
		return tiassResults;
	}

	public void setTiassResults(T tiassResults) {
		this.tiassResults = tiassResults;
	}


	public ValidationError getErrors() {
		return errors;
	}

	public void setErrors(ValidationError errors) {
		this.errors = errors;
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isResponseSet() {
		this.success = (this.errors != null && this.errors.getErrors() != null && this.errors.getErrors().size() > 0)
				|| this.tiassResults != null;
		return this.success;
	}

	public ResponseEntity<TiassResponse<T>> constructResponse() {
		if (this.isResponseSet())
			return ResponseEntity.ok(this);
		else
			return ResponseEntity.badRequest().body(null);
	}

}
