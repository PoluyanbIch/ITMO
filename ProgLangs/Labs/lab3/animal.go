package main

import "fmt"

// Speaker shows how interfaces let different types be used interchangeably.
type Speaker interface {
	Speak() string
}

// Dog implements Speaker
type Dog struct {
	Name string
}

func (d Dog) Speak() string {
	return fmt.Sprintf("%s says: Woof!", d.Name)
}

// Cat implements Speaker
type Cat struct {
	Name string
}

func (c Cat) Speak() string {
	return fmt.Sprintf("%s says: Meow!", c.Name)
}

// DescribeSpeaker uses the Speaker interface polymorphically
func DescribeSpeaker(s Speaker) {
	println(s.Speak())
}
