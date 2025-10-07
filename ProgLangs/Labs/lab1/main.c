#include <stdlib.h>
#include <stdio.h>
#include <memory.h>

 
typedef struct {
    char* name;
    char* surname;
    char* group;
    int age;
    int id;
} Student;
 
Student* createStudent(const char* name, const char* surname, const char* group, int age, int id) {
    Student* student = (Student*)malloc(sizeof(Student));
 
    if (!student) {
        fprintf(stderr, "Memory allocation failed\n");
        return NULL;
    }
 
    student->name = strdup(name);
    student->surname = strdup(surname);
    student->group = strdup(group);
    student->age = age;
    student->id = id;
 
    return student;
}
 

 
void printStudent(const Student* student) {
    if (!student) {
        printf("Student is NULL\n");
        return;
    }
 
    printf("Name: %s\n", student->name);
    printf("Surname: %s\n", student->surname);
    printf("Group: %s\n", student->group);
    printf("Age: %d\n", student->age);
    printf("ID: %d\n", student->id);
}
 
void serializeStudent(const Student* student, const char* filename) {
    if (!student || !filename) {
        fprintf(stderr, "Invalid arguments to serializeStudent\n");
        return;
    }
 
    FILE* file = fopen(filename, "wb");
    if (!file) {
        fprintf(stderr, "Failed to open file for writing\n");
        return;
    }
 
    fwrite(student, sizeof(Student), 1, file);
    fclose(file);
}
 
Student* deserializeStudent(const char* filename) {
    if (!filename) {
        fprintf(stderr, "Invalid filename for deserializeStudent\n");
        return NULL;
    }
 
    FILE* file = fopen(filename, "rb");
    if (!file) {
        fprintf(stderr, "Failed to open file for reading\n");
        return NULL;
    }
 
    Student* student = (Student*)malloc(sizeof(Student));
    if (!student) {
        fprintf(stderr, "Memory allocation failed\n");
        fclose(file);
        return NULL;
    }
 
    fread(student, sizeof(Student), 1, file);
    fclose(file);
 
    return student;
}
 
 
int main() {
    Student* student = createStudent("Andrey", "Ivanov", "W3201", 20, 12345);
    printStudent(student);
    serializeStudent(student, "student.dat");
    free(student);
    Student* loadedStudent = deserializeStudent("student.dat");
    if (loadedStudent) {
        printStudent(loadedStudent);
        free(loadedStudent);
    }
    return 0;
}