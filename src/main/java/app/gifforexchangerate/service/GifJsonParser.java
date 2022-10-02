package app.gifforexchangerate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import lombok.Builder;
import lombok.Getter;

public class GifJsonParser {

    public GifInfo parseNeededData(String json) throws JsonProcessingException {
        GifInfo.GifInfoBuilder dataBuilder = GifInfo.builder();
        ObjectMapper mapper = new ObjectMapper();
        TreeNode root = mapper.readTree(json);

        TreeNode dataRoot = root.get("data");
        TreeNode metaRoot = root.get("meta");

        ValueNode status = (ValueNode) metaRoot.get("status");
        if (!status.asText().equals("200")) {
            throw new IllegalArgumentException("Status != 200");
        }

        ValueNode titleNode = (ValueNode)dataRoot.get("title");
        dataBuilder.title(titleNode.textValue());

        ValueNode urlValue = (ValueNode)dataRoot.get("url");
        dataBuilder.url(urlValue.textValue());

        ObjectNode imagesObjNode = (ObjectNode) dataRoot.get("images");
        ObjectNode fixHeightNode = (ObjectNode) imagesObjNode.get("fixed_height");
        ValueNode urlToGifNode = (ValueNode) fixHeightNode.get("url");
        dataBuilder.urlToGifFile(urlToGifNode.textValue());

        return dataBuilder.build();
    }

    @Builder
    @Getter
    public static class GifInfo {
        private String url;
        private String title;
        private String urlToGifFile;
    }
}
