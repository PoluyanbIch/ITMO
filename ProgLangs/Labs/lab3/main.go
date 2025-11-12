package main

import (
	"fmt"
)

func main() {
	fmt.Println("Go OOP demo — structs, methods, embedding, interfaces")

	// Structs + methods + encapsulation
	p := NewPerson("Ivan", "Ivanov", 30)
	fmt.Println(p.FullName())
	p.SetAge(31)
	fmt.Printf("%s is now %d years old\n", p.FullName(), p.Age())

	// Embedding
	emp := Employee{
		Person: Person{firstName: "Anna", lastName: "Petrova", age: 28},
		Position: "Developer",
		Salary: 80000,
	}
	fmt.Printf("Employee: %s, Position: %s, Salary: %d\n", emp.FullName(), emp.Position, emp.Salary)

	// Interfaces and polymorphism
	var s Shape
	s = Rectangle{Width: 3, Height: 4}
	fmt.Printf("Rectangle area: %.2f, perimeter: %.2f\n", s.Area(), s.Perimeter())

	s = Circle{Radius: 2.5}
	fmt.Printf("Circle area: %.2f, perimeter: %.2f\n", s.Area(), s.Perimeter())

	// Polymorphism with animals
	animals := []Speaker{Dog{Name: "Rex"}, Cat{Name: "Murka"}}
	for _, a := range animals {
		DescribeSpeaker(a)
	}
}
