#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#define INPUT_SIZE 100

/* Gets a substring of '_str' from index '_begin' to '_end'. Stores resultant string into '_dest' */
void substring(char* _dest, char* _src, int _begin, int _end)
{
    int i;
    for (i = _begin; i < _end; i++)  _dest[i - _begin] = _src[i];
    _dest[i - _begin] = '\0';
}

/* Gets the first index of the character '_c' after index '_startIndex' within the string '_str' */
int indexof(char* _str, int _startIndex, char _c)
{
    int output = -1;
    int len = strlen(_str);
    for (int i = _startIndex; i < len; i++)
    {
        if (_str[i] == _c)
        {
            output = i;
            i = len + 1;
        }
    }
    return output;
}

/* Splits the '_input' string into three '_cm', '_arg1', and '_arg2' strings */
void getArgs(char* _input, char* _cmd, char* _arg1, char* _arg2)
{
    int delim = indexof(_input, 0, ' ');
    //printf("delim0: %d\n", delim);

    if (delim > -1)
    {
        substring(_cmd, _input, 0, delim);

        char buffer0[INPUT_SIZE];
        substring(buffer0, _input, delim + 1, strlen(_input) - 1);
        //printf("buffer0: %s\n", buffer0);

        int delim = indexof(buffer0, 0, ' ');
        //printf("delim1: %d\n", delim);

        if (delim > -1)
        {
            substring(_arg1, buffer0, 0, delim);
            substring(_arg2, buffer0, delim + 1, strlen(buffer0));
        }
        else strcpy(_arg1, buffer0);
    }
    else substring(_cmd, _input, 0, strlen(_input) - 1);
}

void unixcmd(const char* _cmd, char* _arg1, char* _arg2)
{
	char buffer[INPUT_SIZE];
	sprintf(buffer, _cmd, _arg1, _arg2);
	system(buffer);
}

int main()
{
    bool loop = true;
    while (loop)
    {
        char input[INPUT_SIZE];
        char command[INPUT_SIZE], arg1[INPUT_SIZE], arg2[INPUT_SIZE];

        input[0] = '\0';
        command[0] = '\0';
        arg1[0] = '\0';
        arg2[0] = '\0';

        printf("Input a command (Type Ctrl-C to exit): ");
        fgets(input, INPUT_SIZE, stdin);

        getArgs(input, command, arg1, arg2);
       
        if (!strcmp("cd", command))
        {
            //unixcmd("cd %s %s", arg1, arg2);
	    chdir(arg1);
        }
        else if (!strcmp("dir", command))
        {
            unixcmd("ls %s %s", arg1, arg2);
        }
        else if (!strcmp("type", command))
        {
            unixcmd("cat %s %s", arg1, arg2);
        }
        else if (!strcmp("del", command))
        {
            unixcmd("rm %s %s", arg1, arg2);
        }
        else if (!strcmp("ren", command))
        {
            unixcmd("mv %s %s", arg1, arg2);
        }
        else if (!strcmp("copy", command))
        {
            unixcmd("cp %s %s", arg1, arg2);
        }
        else printf("Unknown command!");

        printf("\n\n");
    }
}
