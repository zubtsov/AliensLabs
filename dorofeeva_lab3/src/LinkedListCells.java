public class LinkedListCells implements Cloneable{
    //проверка корректности index не делается
    private static class MatrixCell implements Cloneable {
        public MatrixCell next;
        public MatrixCell prev;
        public int index;
        public double value;

        @Override
        protected MatrixCell clone() throws CloneNotSupportedException {
            return (MatrixCell)super.clone();
        }
    }
    private MatrixCell current;
    private MatrixCell tail;
    private MatrixCell head;
    private int size;

    public int getSize() {
        return size;
    }

    public void next() {
        if (current != tail) current = current.next;
    }

    public void prev() {
        if (current != head) current = current.prev;
    }

    public double getValue() {
        return current.value;
    }

    public void setValue(double value) {
        if (value == 0) {
            current.next.prev = current.prev;
            current.prev.next = current.next;
            current = current.next;
        }
        current.value = value;
    }

    public double get(int index) {
        if (current == null) {
            return 0.0;
        }
        MatrixCell c = current;
        boolean forward = c.index<index?true:false;
        do {
            if (c==null)
                return 0.0;
            if(c.index == index) {
                current = c;
                return c.value;
            } else if (c.index > index) {
                if(forward)
                    return 0.0;
                c=c.prev;
                forward = false;
            } else if (c.index < index) {
                if (!forward)
                    return 0.0;
                c=c.next;
                forward=true;
            }
        } while(true);
    }

    public double set(int index, double value) {
        if (head == null) {
            if (value != 0.0) {
                tail = head = current = new MatrixCell();
                current.index = index;
                current.value = value;
                size++;

            }
            return value;
        }
        MatrixCell c = current;
        boolean forward = c.index < index ? true : false;
        do {
            if (c.index == index) {
                if (value != 0) {
                    c.value = value;
                    current = c;
                    return value;
                } else { // можно сделать проверку < 10e-15
                    //удаляем текущий элемент списка
                    if(c.prev != null) {
                        c.prev.next = c.next;
                        current = c.prev;
                    }
                    if(c.next != null) {
                        current = c.next;
                        c.next.prev = c.prev;
                    }
                    size--;
                    if (size == 0) {
                        head = tail = current = null;
                    }
                    return value;
                }
            }
            else if (c.index > index) {
                if (forward) {
                    if(value==0.0)
                        return 0.0;
                    //добавление нового элемента до
                    MatrixCell newCell = new MatrixCell();
                    newCell.value = value;
                    newCell.index = index;
                    newCell.next = c;
                    newCell.prev = c.prev;

                    newCell.prev.next = newCell;
                    c.prev = newCell;

                    current = newCell;
                    size++;
                    return value;
                }
                if (c.prev != null) {
                    c = c.prev;
                    forward = false;
                } else {
                    if(value==0)
                        return 0.0;
                    c.prev = new MatrixCell();
                    c.prev.next = c;
                    c=c.prev;
                    c.index=index;
                    c.value = value;
                    current = c;
                    head = c;
                    size++;
                    return value;
                }
            }
            else if (c.index < index) {
                if (!forward) {
                    if(value==0.0)
                        return 0.0;
                    //добавление нового элемента после
                    MatrixCell newCell = new MatrixCell();
                    newCell.value = value;
                    newCell.index = index;
                    newCell.next = c.next;
                    newCell.prev = c;
                    c.next = newCell;
                    newCell.next.prev = newCell;
                    current = newCell;
                    size++;
                    return value;
                }
                if (c.next != null) {
                    c = c.next;
                    forward = true;
                } else {
                    if(value==0)
                        return 0.0;
                    c.next = new MatrixCell();
                    c.next.prev = c;
                    c=c.next;

                    c.index=index;
                    c.value = value;
                    current = c;
                    tail = c;
                    size++;
                    return value;
                }
            }
        } while (true);
    }

    @Override
    protected LinkedListCells clone() throws CloneNotSupportedException {
        LinkedListCells llc = new LinkedListCells();
        llc.size = size;
        llc.current = llc.head = head.clone();
        MatrixCell c = head;
        MatrixCell c2 = llc.head;
        do {
            c2.next = c.next.clone();
            c2.next.prev = c2;
            c2 = c2.next;
            c=c.next;
        } while(c.next != null);
        llc.tail = c2;
        llc.current = llc.head;

        return llc;
    }
}