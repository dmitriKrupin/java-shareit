package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOutPost;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ItemMapper {
    public static ItemDtoIn toItemDto(Item item) {
        return new ItemDtoIn(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? item.getOwner().getId() : null,
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemDtoOutPost toItemInfoDto(
            Item item,
            Booking lastBooking,
            Booking nextBooking,
            List<CommentDtoOut> comment) {
        return new ItemDtoOutPost(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                new ItemDtoOutPost.BookingDto(
                        lastBooking.getId(),
                        lastBooking.getStart(),
                        lastBooking.getEnd(),
                        lastBooking.getBooker().getId()),
                new ItemDtoOutPost.BookingDto(
                        nextBooking.getId(),
                        nextBooking.getStart(),
                        nextBooking.getEnd(),
                        nextBooking.getBooker().getId()),
                comment
        );
    }

    public static ItemDtoOutPost toItemInfoDto(
            Item item,
            Booking lastBooking,
            List<CommentDtoOut> comment) {
        return new ItemDtoOutPost(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                new ItemDtoOutPost.BookingDto(
                        lastBooking.getId(),
                        lastBooking.getStart(),
                        lastBooking.getEnd(),
                        lastBooking.getBooker().getId()),
                null,
                comment
        );
    }

    public static ItemDtoOutPost toItemInfoDto(
            Item item,
            List<CommentDtoOut> comment) {
        return new ItemDtoOutPost(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                comment
        );
    }

    public static Item toItem(ItemDtoIn itemDtoIn) {
        return new Item(
                itemDtoIn.getId(),
                itemDtoIn.getName(),
                itemDtoIn.getDescription(),
                itemDtoIn.getAvailable()
        );
    }

    public static List<ItemDtoIn> toItemsListDto(List<Item> itemsList) {
        List<ItemDtoIn> itemsDtoList = new ArrayList<>();
        for (Item entry : itemsList) {
            itemsDtoList.add(ItemMapper.toItemDto(entry));
        }
        return itemsDtoList;
    }

    public static List<ItemDtoOutPost> toItemsListDtoOutPost(List<Item> itemsList) {
        List<ItemDtoOutPost> itemsListDtoOutPost = new ArrayList<>();
        for (Item entry : itemsList) {
            itemsListDtoOutPost.add(ItemMapper.toItemInfoDto(
                    entry, new Booking(), new Booking(), new ArrayList<>()));
        }
        return itemsListDtoOutPost;
    }

    public static CommentDtoOut toCommentDtoOut(Comment comment) {
        return new CommentDtoOut(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<CommentDtoOut> toCommentDtoOut(List<Comment> comments) {
        List<CommentDtoOut> commentsDtoOutList = new ArrayList<>();
        for (Comment entry : comments) {
            commentsDtoOutList.add(toCommentDtoOut(entry));
        }
        return commentsDtoOutList;
    }
}
