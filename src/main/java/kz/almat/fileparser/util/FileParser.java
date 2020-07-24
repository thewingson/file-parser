package kz.almat.fileparser.util;

import kz.almat.fileparser.pojo.xls.AbstractXlsEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Almat
 * created on 24/07/2020
 */
public interface FileParser {

    <E extends AbstractXlsEntity> List<E> read(MultipartFile file, Class<E> entity) throws IOException;

    void write(MultipartFile file, Class<? extends AbstractXlsEntity> entity);

}
