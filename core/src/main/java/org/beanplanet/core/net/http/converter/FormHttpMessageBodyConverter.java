package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.lang.ParameterisedTypeReference;
import org.beanplanet.core.net.http.*;
import org.beanplanet.core.util.MultiValueMap;
import org.beanplanet.core.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.beanplanet.core.net.http.MediaTypeUtil.generateMultipartBoundary;

/**
 * An HTTP message body converter capable of converting 'form' data, specifically: {@link org.beanplanet.core.util.MultiValueMap<String, Object>}
 * to/from <code>multipart/form-data</code> media types.
 */
@org.beanplanet.core.net.http.converter.annotations.HttpMessageBodyConverter
public class FormHttpMessageBodyConverter<C extends Collection<Object>> extends AbstractHttpMessageBodyConverter<MultiValueMap<String, Object, C>> implements HttpMessageBodyConverterRegistryAware {
    private HttpMessageBodyConverterRegistry converterRegistry;

    /**
     * Creates a new form data message body converter, with default configuration.
     */
    public FormHttpMessageBodyConverter() {
        super(
                MediaTypes.Multipart.FORM_DATA
        );
    }

    /**
     * Whether this handler supports reading from or writing to a given type.
     *
     * @param type the type to be tested for support by this handler.
     * @return true because this handler will attempt to marshall from/to any type, in context of the supported
     * media types.
     */
    @Override
    public boolean supports(Type type) {
        return type instanceof Class<?> && MultiValueMap.class.isAssignableFrom((Class<?>)type);
    }

    /**
     * Reads an object from the given input message.
     *
     * @param type    the type of object to be read from the input.
     * @param message the request message from which te object is to be read.
     * @return the object read.
     */
    @Override
    public MultiValueMap<String, Object, C> convertFrom(ParameterisedTypeReference<MultiValueMap<String, Object, C>> type, HttpMessage message) {
        throw new UnsupportedOperationException();
    }

    /**
     * Writes the given object to the output message.
     *
     * @param parts          the multipart parts to be written.
     * @param messageHeaders the headers of the message where the object is to be written.
     */
    @Override
    public Resource convertTo(MultiValueMap<String, Object, C> parts, HttpMessageHeaders messageHeaders) {
        try {
            MediaType contentType = messageHeaders.getContentType().orElse(MediaTypes.Multipart.FORM_DATA);

            byte[] boundary = generateMultipartBoundary();
            contentType = new MediaType(contentType.getType(), contentType.getSubtype(),
                    contentType.getParameters().combine(Parameters.singleton("boundary", new String(boundary, StandardCharsets.US_ASCII))));
            messageHeaders.setContentType(contentType);

            ByteArrayOutputStreamResource baosr = new ByteArrayOutputStreamResource();
            writeParts(baosr.getOutputStream(), parts, boundary, messageHeaders);
            writeCrLf(baosr.getOutputStream(), boundary);

            return baosr;
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    private void writeParts(OutputStream os, MultiValueMap<String, ?, C> parts, byte[] boundary, HttpMessageHeaders messageHeaders) throws IOException {
        for (Map.Entry<String, C> entry : parts.entrySet()) {
            String partName = entry.getKey();
            for (Object part : entry.getValue()) {
                writeBoundary(os, boundary);
                writePart(os, partName, part, messageHeaders);
                writeCrLf(os);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void writePart(OutputStream os, String partName, Object part, HttpMessageHeaders messageHeaders) throws IOException {
        Class<Object> partType = (Class<Object>) part.getClass();
        HttpHeaders partHeaders = new HttpHeaders();
        Charset partCharset = messageHeaders.getCharset().orElse(null);
        final String partFilename = filenameFor(part);

        Resource partBody = getConverterRegistry().findToConverters(null, partType)
                                                  .findFirst()
                                                  .orElseThrow(() -> new HttpMessageBodyConversionException("Unable to write request body: no suitable " +
                                                          "HTTP message converter found for multipart value type [" + partType.getName() + "]"))
                                                  .convertTo(part, partHeaders);
        ContentDisposition contentDisposition = ContentDisposition.builder()
                                                                  .formData()
                                                                  .name(partName)
                                                                  .filenameIf(StringUtil::isNotBlank, partFilename, partCharset)
                                                                  .build();
        partHeaders.setContentDisposition(contentDisposition.getCanonicalForm());

        //--------------------------------------------------------------------------------------------------------------
        // Write part headers
        //--------------------------------------------------------------------------------------------------------------
        for (Map.Entry<String, List<String>> entry : partHeaders.getAll().entrySet()) {
            byte[] headerNameBytes = entry.getKey().getBytes();
            for (String headerValueString : entry.getValue()) {
                byte[] headerValueBytes = headerValueString.getBytes();
                os.write(headerNameBytes);
                os.write(':');
                os.write(' ');
                os.write(headerValueBytes);
                writeCrLf(os);
            }
        }
        writeCrLf(os);

        //--------------------------------------------------------------------------------------------------------------
        // Write part body
        //--------------------------------------------------------------------------------------------------------------
        try (InputStream partBodyIs = partBody.getInputStream()) {
            IoUtil.transfer(partBodyIs, os);
        }
    }

    protected String filenameFor(Object part) {
        return part instanceof Resource ? ((Resource) part).getName() : null;
    }

    private static void writeBoundary(OutputStream os, byte[] boundary) throws IOException {
        os.write('-');
        os.write('-');
        os.write(boundary);
        writeCrLf(os);
    }

    private static void writeCrLf(OutputStream os) throws IOException {
        os.write(IoUtil.CRLF.getBytes());
    }

    private static void writeCrLf(OutputStream os, byte[] boundary) throws IOException {
        os.write('-');
        os.write('-');
        os.write(boundary);
        os.write('-');
        os.write('-');
    }

    public HttpMessageBodyConverterRegistry getConverterRegistry() {
        return converterRegistry != null ? converterRegistry : SystemHttpMessageBodyConverterRegistry.getInstance();
    }

    @Override
    public HttpMessageBodyConverterRegistry setRegistry(HttpMessageBodyConverterRegistry registry) {
        return this.converterRegistry = registry;
    }
}
