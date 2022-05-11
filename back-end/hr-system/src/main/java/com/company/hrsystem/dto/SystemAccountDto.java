package com.company.hrsystem.dto;

import com.company.hrsystem.model.SystemAccountModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemAccountDto extends SystemAccountModel {

	private static final long serialVersionUID = 1L;

	public SystemAccountDto(Integer systemAccountId, String systemEmail, String systemPassword, Boolean deletedFlag,
			String createdAt, String updatedAt) {
		super(systemAccountId, systemEmail, systemPassword, deletedFlag, createdAt, updatedAt);
	}

}
