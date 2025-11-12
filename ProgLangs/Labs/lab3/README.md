Go OOP demo

This small project demonstrates object-oriented programming concepts in Go:

- Structs and methods
- Encapsulation via unexported fields and getter/setter methods
- Embedding (composition) to share behaviour similar to inheritance
- Interfaces and polymorphism

Files:
- main.go — entrypoint and demo scenarios
- person.go — Person and Employee types (methods, encapsulation, embedding)
- shape.go — Shape interface with Rectangle and Circle implementations
- animal.go — Speaker interface with Dog and Cat demonstrating polymorphism

Run:

```powershell
# from project root (where go.mod is)
go run .
```

Expected output: prints demonstration lines for FullName, updated age, employee info, areas/perimeters, and animal sounds.
