package com.congress.storage;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.*;

/**
 * Adapts a {@link org.springframework.core.io.Resource} to a
 * {@link org.springframework.web.multipart.MultipartFile}.
 *
 * @author Laurens Fridael
 */
public class ResourceMultipartFile implements MultipartFile {

    private final Resource resource;

    private final String name;
    private final String contentType;
    private final int size;
    private String originalFilename;

    /**
     * Constructs a new instance.
     *
     * @param resource    The resource.
     * @param contentType The content type or {@code null} if unspecified.
     * @param size        The resource size.
     */
    public ResourceMultipartFile(@NotNull Resource resource, @NotNull String name, @Nullable String contentType, long size) {
        Assert.notNull(resource, "Resource cannot be null.");
        Assert.hasText(name, "Name cannot be empty.");
        Assert.isTrue(size >= 0, "Size cannot be less than 0.");
        Assert.isTrue(size <= Integer.MAX_VALUE, String.format("Size cannot be larger than %d.", Integer.MAX_VALUE));

        this.resource = resource;
        this.name = name;

        this.contentType = contentType;
        this.size = (int) size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return resource.exists() && size > 0;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(size);
        FileCopyUtils.copy(getInputStream(), buffer);
        return buffer.toByteArray();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException {
        FileCopyUtils.copy(getInputStream(), new FileOutputStream(dest));
    }
}