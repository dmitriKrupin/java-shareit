package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemMapper {
    public static ItemDtoOut toItemDtoOut(Item item) {
        return new ItemDtoOut(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getItemRequest() != null ? item.getItemRequest().getId() : null
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
                new BookingDtoOut(
                        lastBooking.getId(),
                        lastBooking.getStart(),
                        lastBooking.getEnd(),
                        lastBooking.getBooker().getId()),
                new BookingDtoOut(
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
                new BookingDtoOut(
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

    public static ItemDtoIn toItemDtoIn(Item item) {
        return new ItemDtoIn(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static List<ItemDtoOut> toItemsDtoOutList(List<Item> itemsList) {
        List<ItemDtoOut> itemsDtoList = new ArrayList<>();
        for (Item entry : itemsList) {
            itemsDtoList.add(ItemMapper.toItemDtoOut(entry));
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

    public static CommentDtoIn toCommentDtoIn(Comment comment) {
        return new CommentDtoIn(
                comment.getId(),
                comment.getText()
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
