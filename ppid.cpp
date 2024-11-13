#include <iostream>
#include <unistd.h> // For fork() and getpid()
#include <sys/types.h>
using namespace std;

int main()
{
    pid_t pid = fork(); // Create a child process

    if (pid < 0)
    {
        // If fork() fails
        cout << "Fork failed!" << endl;
        return 1;
    }

    if (pid == 0)
    {
        // Child process
        cout << "Child Process: " << endl;
        cout << "Child PID: " << getpid() << endl;
        cout << "Parent PID (PPID) of Child: " << getppid() << endl;
    }
    else
    {
        // Parent process
        cout << "Parent Process: " << endl;
        cout << "Parent PID: " << getpid() << endl;
        cout << "Parent of Parent Process: " << getppid() << endl;
    }

      return 0;
}