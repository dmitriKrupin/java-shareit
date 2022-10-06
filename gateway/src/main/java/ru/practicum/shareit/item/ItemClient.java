package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(ItemDtoIn itemDtoIn, long userId) {
        return post("", userId, itemDtoIn);
    }

    public ResponseEntity<Object> addComment(CommentDtoIn commentDtoIn, long itemId, long userId) {
        return post("/" + itemId + "/comment", userId, commentDtoIn);
    }

    public ResponseEntity<Object> updateItem(long itemId, ItemDtoIn updateItemDtoIn, long userId) {
        return patch("/" + itemId, userId, updateItemDtoIn);
    }

    public ResponseEntity<Object> findAllItemDtoByUserId(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("", userId, parameters);
    }

    public ResponseEntity<Object> getItemsDtoBySearch(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/search?text=" + text, null, parameters);
    }

    public ResponseEntity<Object> findItemById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }
}
