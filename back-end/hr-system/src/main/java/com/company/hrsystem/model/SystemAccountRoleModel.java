package com.company.hrsystem.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
abstract class SystemAccountRoleModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer systemAccountId;

	private Integer systemRoleId;

	private Boolean deletedFlag;

	private Date createdAt;

	private Date updatedAt;

}