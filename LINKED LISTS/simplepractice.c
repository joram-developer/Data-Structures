#include <stdio.h>
#include <stdlib.h>

int main() {

    int *p;// Declare a pointer to int

    p = malloc(sizeof(int));// Allocate memory for an int

    if (p == NULL) {// Check if memory allocation was successful
        printf("Memory allocation failed.\n");
        return 1;//return 1 means that the program encountered an error
    }

    *p = 25;// Assign a value to the allocated memory

    printf("Value: %d\n", *p);

    free(p);// Free the allocated memory

    return 0;
}