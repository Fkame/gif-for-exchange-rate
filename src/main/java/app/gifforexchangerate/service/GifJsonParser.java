package app.gifforexchangerate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import lombok.Builder;

public class GifJsonParser {

    public GifInfo parseNeededData(String json) throws JsonProcessingException {
        GifInfo.GifInfoBuilder dataBuilder = GifInfo.builder();
        ObjectMapper mapper = new ObjectMapper();
        TreeNode root = mapper.readTree(json);

        ValueNode titleNode = (ValueNode)root.get("title");
        dataBuilder.title(titleNode.textValue());

        ValueNode urlValue = (ValueNode)root.get("url");
        dataBuilder.url(urlValue.textValue());

        ObjectNode imagesObjNode = (ObjectNode) root.get("images");
        ObjectNode fixHeightNode = (ObjectNode) imagesObjNode.get("fixed_height");
        ValueNode urlToGifNode = (ValueNode) fixHeightNode.get("url");
        dataBuilder.urlToGifFile(urlToGifNode.textValue());

        return dataBuilder.build();
    }

    @Builder
    public static class GifInfo {
        String url;
        String title;
        String urlToGifFile;
    }
}
