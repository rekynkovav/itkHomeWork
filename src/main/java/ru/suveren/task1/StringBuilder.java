package ru.suveren.task1;

public class StringBuilder {
    int capacity;
    byte[] arrayString;
    int count = 0;

    public StringBuilder() {
        capacity = 16;
        arrayString = new byte[capacity];
    }

    public StringBuilder(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
            arrayString = new byte[this.capacity];
        } else {
            this.capacity = 16;
            arrayString = new byte[this.capacity];
        }
    }

    public StringBuilder undo(String str) {
        if (str == null) {
            return this;
        }
        int length = str.length();
        if (length <= arrayString.length - count) {
            System.arraycopy(str.getBytes(), 0, arrayString, count, length);
            count += length;
            return this;
        }
        neededSpace(length);
        byte[] tempArray = new byte[capacity];
        System.arraycopy(arrayString, 0, tempArray, 0, count);
        System.arraycopy(str.getBytes(), 0, tempArray, count, length);
        count += length;
        arrayString = tempArray;
        return this;
    }

    private void neededSpace(int length) {
        int neededSpace = count + length;

        while (capacity < neededSpace) {
            capacity = (capacity * 2) + 2;
        }
    }

    public String convertToString() {
        return new String(arrayString, 0, count);
    }
}
