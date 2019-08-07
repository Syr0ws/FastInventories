package fr.syrows.fastinvs.contents;

import com.google.gson.*;
import fr.syrows.fastinvs.FastInventory;
import fr.syrows.fastinvs.FastInvsAPI;
import fr.syrows.fastinvs.inventories.InventoryContents;
import fr.syrows.fastinvs.loader.LoadedItem;
import fr.syrows.fastinvs.tools.SlotIterator;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class Pagination {

    private SlotIterator iterator;
    private ClickableItem previousPage, nextPage;
    private int previousPageSlot, nextPageSlot;

    private int page;

    private ClickableItem[] contents;

    public void setPageContents(FastInventory inv) {

        InventoryContents contents = inv.getInventoryContents();

        this.iterator.reset();

        ClickableItem[] pageContents = getPageContents();

        int index = 0;

        boolean hasNext = this.iterator.hasNext();

        while (hasNext) {

            hasNext = this.iterator.hasNext();

            int row = this.iterator.getRow(), column = this.iterator.getColumn();

            contents.set(row, column, pageContents[index]);

            this.iterator.next();

            index++;
        }
    }

    public void setPages(FastInventory inv) {

        InventoryContents contents = inv.getInventoryContents();

        contents.set(this.previousPageSlot, this.previousPage);
        contents.set(this.nextPageSlot, this.nextPage);
    }

    public void clearPage(FastInventory inv) {

        InventoryContents contents = inv.getInventoryContents();

        this.iterator.reset();

        boolean hasNext = this.iterator.hasNext();

        while (hasNext) {

            hasNext = this.iterator.hasNext();

            int row = this.iterator.getRow(), column = this.iterator.getColumn();

            contents.set(row, column, null);

            iterator.next();
        }
    }

    public ClickableItem[] getPageContents() {

        int slots = this.iterator.countSlots(), index = this.page == 1 ? 0 : slots * (this.page - 1);

        ClickableItem[] pageContents = new ClickableItem[slots];

        for(int i = index, contentSlot = 0; i < index + slots; i++, contentSlot++)
            pageContents[contentSlot] = i < this.contents.length ? this.contents[i] : null;

        return pageContents;
    }

    public void previous() { if(hasPrevious()) this.page--; }

    public void next() { if(hasNext()) this.page++; }

    public boolean hasPrevious() { return this.page > 1; }

    public boolean hasNext() {
        int maxPage = (int) Math.ceil((double) this.contents.length / (double) this.iterator.countSlots());
        return this.page < maxPage;
    }

    public boolean isFirst() { return this.page == 1; }

    public boolean isLast() {
        int maxPage = (int) Math.ceil((double) this.contents.length / (double) this.iterator.countSlots());
        return this.page == maxPage;
    }

    public ClickableItem getPreviousPage() {
        return previousPage;
    }

    public ClickableItem getNextPage() {
        return nextPage;
    }

    public int getPreviousPageSlot() {
        return previousPageSlot;
    }

    public int getNextPageSlot() {
        return nextPageSlot;
    }

    public int getPage() {
        return page;
    }

    public void setContents(ClickableItem[] contents) {
        this.contents = contents != null ? contents : new ClickableItem[0];
    }

    public ClickableItem[] getContents() {
        return contents;
    }

    public static class PaginationBuilder {

        private SlotIterator iterator;
        private ClickableItem previousPage, nextPage;
        private int previousPageSlot, nextPageSlot;

        private ClickableItem[] contents;

        public PaginationBuilder(SlotIterator iterator) {

            if(iterator == null)
                throw new IllegalArgumentException("SlotIterator cannot be null.");

            this.iterator = iterator;
        }

        public PaginationBuilder withPreviousPage(ItemStack previousPage, int slot) {
            this.previousPage = new ClickableItem("previousPage", previousPage);
            this.previousPageSlot = slot;
            return this;
        }

        public PaginationBuilder withNextPage(ItemStack nextPage, int slot) {
            this.nextPage = new ClickableItem("nextPage", nextPage);
            this.nextPageSlot = slot;
            return this;
        }

        public PaginationBuilder withContents(ClickableItem[] contents) {
            this.contents = contents;
            return this;
        }

        public Pagination build() {

            Pagination pagination = new Pagination();

            pagination.iterator = this.iterator;
            pagination.previousPage = this.previousPage;
            pagination.nextPage = this.nextPage;
            pagination.previousPageSlot = this.previousPageSlot;
            pagination.nextPageSlot = this.nextPageSlot;

            pagination.contents = this.contents != null
                    ? this.contents : new ClickableItem[iterator.countSlots()];

            pagination.page = 1;

            return pagination;
        }
    }

    public static class PaginationDeserializer implements JsonDeserializer<Pagination> {

        @Override
        public Pagination deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {

            JsonObject object = element.getAsJsonObject();

            LoadedItem previousPage = FastInvsAPI.getGson()
                    .fromJson(object.getAsJsonObject("previousPage"), LoadedItem.class);

            LoadedItem nextPage = FastInvsAPI.getGson()
                    .fromJson(object.getAsJsonObject("nextPage"), LoadedItem.class);

            Pagination pagination = new Pagination();

            pagination.previousPage = new ClickableItem("previousPage", previousPage.getItemStack());
            pagination.previousPageSlot = previousPage.getSlots()[0];

            pagination.nextPage = new ClickableItem("nextPage", nextPage.getItemStack());
            pagination.nextPageSlot = nextPage.getSlots()[0];

            pagination.page = 1;

            return pagination;
        }
    }
}
