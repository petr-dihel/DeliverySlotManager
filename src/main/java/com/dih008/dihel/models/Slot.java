package com.dih008.dihel.models;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.dih008.dihel.annotations.Listable;
import com.dih008.dihel.annotations.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Slot {
	
	@Id
	@Listable
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Listable
	@Type(type = "datetime-local")
	private LocalDateTime since;
	
	@Listable
	@Type(type = "text")
	private int daySlotNumber;
	
	public Slot() {}
	
	@JsonIgnore
	@OneToMany
	private List<Package> packages;
	
	public Slot(int id, LocalDateTime since, int daySlotNumber) {
		this.id = id;
		this.since = since;
		this.daySlotNumber = daySlotNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getSince() {
		return since;
	}

	public void setSince(LocalDateTime since) {
		this.since = since;
	}

	public int getDaySlotNumber() {
		return daySlotNumber;
	}

	public void setDaySlotNumber(int daySlotNumber) {
		this.daySlotNumber = daySlotNumber;
	}
	 
}
