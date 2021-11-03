/*
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <http://unlicense.org>
 */
#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <signal.h>
#include <pthread.h>
#include <unistd.h>

// Global variables used to control the service. DO NOT USE IT AS AN EXAMPLE OF
// GOOD PROGRAMING OR IT WILL COME BACK LATER TO EAT YOU ALIVE (OR EVEN DEAD).
volatile int return_code = 0;
// Hint: Those variables have been marked as volatile to avoid some compiler
// optimizations and static code analysis from detecting and removing those
// errors/dead codes.
volatile int a = 0;
volatile int b = 0;
volatile char *null_ptr = NULL;

// A mutex and a condition variable used to halt the program execution until a
// signal comes. Once again, do not use global variables unless it is
// unavoidable.
pthread_mutex_t mutex;
pthread_cond_t condition = PTHREAD_COND_INITIALIZER;

/**
 * The signal handler. It must be registered by calling signal() function
 * somewhere else in this code.
 * 
 * @param[in] signal: The signal received.
 */
void on_signal(int signal)
{
    assert(pthread_mutex_lock(&mutex) == 0);
    switch (signal)
    {
    case SIGTERM:
        // Normal termination.
        return_code = 0;
        break;
    case SIGUSR1:
        // Division by zero.
        a = a / b;
        break;
    case SIGUSR2:
        // Access violation.
        *null_ptr = 0;
        break;
    }
    printf("Signal received: %d\n", signal);
    assert(pthread_cond_signal(&condition) == 0);
    assert(pthread_mutex_unlock(&mutex) == 0);
}

/**
 * Main function. It sets up the program and wait for a signal before exit.
 */
int main()
{
    // Displays my PID.
    printf("My PID is %d\n", getpid());

    // Creates the mutex used to control the access to the condition variable.
    assert(pthread_mutex_init(&mutex, NULL) == 0);

    // Register the signal handlers
    signal(SIGTERM, on_signal);
    signal(SIGUSR1, on_signal);
    signal(SIGUSR2, on_signal);

    // Wait for the signal
    assert(pthread_mutex_lock(&mutex) == 0);
    assert(pthread_cond_wait(&condition, &mutex) == 0);
    assert(pthread_mutex_unlock(&mutex) == 0);
    fprintf(stderr, "Terminating...\n");

    // Cleanup
    assert(pthread_mutex_destroy(&mutex) == 0);
    return return_code;
}