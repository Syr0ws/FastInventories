package fr.syrows.fastinvs.tools;

public class SlotIterator {

    private IteratorType type;
    private int startRow, startColumn, endRow, endColumn, row, column;

    public void next() {

        if(!hasNext()) return;

        if(this.type == IteratorType.VERTICAL) {

            if(this.row != this.endRow) {
                this.row++;
                return;
            }
            this.row = this.startRow;

            if(this.column != this.endColumn) this.column++;

        } else if(this.type == IteratorType.HORIZONTAL) {

            if(this.column != this.endColumn) {
                this.column++;
                return;
            }
            this.column = this.startColumn;

            if(this.row != this.endRow) this.row++;
        }
    }

    public void previous() {

        if(!hasPrevious()) return;

        if(this.type == IteratorType.VERTICAL) {

            if(this.row != this.startRow) {
                this.row--;
                return;
            }
            this.row = this.endRow;

            if(this.column != this.startColumn) this.column--;

        } else if(this.type == IteratorType.HORIZONTAL) {

            if(this.column != this.startColumn) {
                this.column--;
                return;
            }
            this.column = this.endColumn;

            if(this.row != this.startRow) this.row--;
        }
    }

    public void reset() {
        this.row = this.startRow;
        this.column = this.startColumn;
    }

    public int countSlots() { return (this.endRow - this.startRow + 1) * (this.endColumn - this.startColumn + 1); }

    public boolean hasPrevious() { return !(this.row == this.startRow && this.column == this.startColumn); }

    public boolean hasNext() { return !(this.row == this.endRow && this.column == this.endColumn); }

    public IteratorType getType() {
        return type;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public enum IteratorType {

        VERTICAL, HORIZONTAL;
    }

    public static SlotIterator newIterator(IteratorType type, int startRow, int startColumn, int endRow, int endColumn) {

        SlotIterator iterator = new SlotIterator();

        iterator.type = type;
        iterator.startRow = startRow;
        iterator.startColumn = startColumn;
        iterator.endRow = endRow;
        iterator.endColumn = endColumn;

        iterator.row = startRow;
        iterator.column = startColumn;

        return iterator;
    }
}
