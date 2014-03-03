#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <wchar.h>
#include <windows.h>

int main() {
	wchar_t wjargs[1024];
	
	wcscat(wjargs,L"-Xmx512m -jar \"");
	{
		wchar_t path[1024];
		wmemset(path,0,1024);
		GetModuleFileNameW(NULL,path,1024);
		//find last occurance of \, replace it with 0 (terminator)
		for(int i=1023;i>=0;--i) {
			if((int)path[i]==92) {
				path[i] = 0;
				break;
			}
		}
		wcscat(wjargs,path);
	}
	//our jar file
	wcscat(wjargs,L"\\lib\\cddatse.jar\"");
	
	//TODO check if they even have java installed, if not, yell at them.
	ShellExecuteW(NULL,L"open",L"javaw.exe",wjargs,NULL,SW_SHOWNORMAL);
	return 0;
}