CC = gcc
CFLAGS = -std=gnu99 -Wall -O3 -g0
LFLAGS = -s -mwindows -static-libgcc $(CFLAGS)
O_FILES = $(patsubst %.c, %.o, $(wildcard *.c))
RES_FILES = $(patsubst %.rc, %.res, $(wildcard *.rc))
OBJECTS = $(O_FILES) $(RES_FILES)
TARGET = cddatse.exe

default: $(TARGET)

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

%.res: %.rc
	windres $< -O coff -o $@

$(TARGET): $(OBJECTS)
	$(CC) $(OBJECTS) $(LFLAGS) -Wall -o $@

clean:
	rm -f *.o
	rm -f *.res
	rm -f $(TARGET)