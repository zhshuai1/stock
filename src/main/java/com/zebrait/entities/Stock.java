package com.zebrait.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Wither;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@Wither
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Stock {

	public Stock(String code, Date date, float begin, float end, float high, float low, float delta) {
		super();
		this.code = code;
		this.date = date;
		this.begin = begin;
		this.end = end;
		this.high = high;
		this.low = low;
		this.delta = delta;
	}

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
