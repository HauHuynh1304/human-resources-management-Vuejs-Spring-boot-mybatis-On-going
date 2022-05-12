package com.company.hrsystem.dto;

import java.io.Serializable;

import com.company.hrsystem.model.CommentModel;

public class CommentDto extends CommentModel implements Serializable {

	private static final long serialVersionUID = 1L;

	public CommentDto(Integer supervisorActionId, Integer approverActionId, String commentDetail) {
		super(supervisorActionId, approverActionId, commentDetail);
	}

}
