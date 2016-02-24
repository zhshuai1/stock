package com.zebrait.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Wither;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@Wither
@AllArgsConstructor
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	Integer id;

	@Column(name = "code")
	String code;

	@Column(name = "date")
	Date date;

	@Column(name = "begin")
	Float begin;

	@Column
	Float end;

	@Column
	Float high;

	@Column
	Float low;

	@Column
	Float delta;
}
