package main

import "fmt"

// Person demonstrates a basic struct with encapsulation via unexported fields
// and methods to access them.
type Person struct {
	firstName string // unexported -> encapsulated
	lastName  string
	age       int
}

// NewPerson is a constructor-style helper (not required, but idiomatic)
func NewPerson(first, last string, age int) Person {
	return Person{firstName: first, lastName: last, age: age}
}

// FullName is a method with a value receiver.
func (p Person) FullName() string {
	return fmt.Sprintf("%s %s", p.firstName, p.lastName)
}

// SetAge uses a pointer receiver to mutate the struct.
func (p *Person) SetAge(newAge int) {
	if newAge >= 0 {
		p.age = newAge
	}
}

// Age exposes the age field (read-only getter).
func (p Person) Age() int {
	return p.age
}

// Employee shows struct embedding to achieve composition / "inheritance-like" behaviour
type Employee struct {
	Person // embedding Person -> Employee has Person's methods
	Position string
	Salary   int
}

// GiveRaise demonstrates adding behaviour specific to Employee
func (e *Employee) GiveRaise(amount int) {
	e.Salary += amount
}
