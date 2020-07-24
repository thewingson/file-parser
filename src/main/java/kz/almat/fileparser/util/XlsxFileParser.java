package kz.almat.fileparser.util;

import kz.almat.fileparser.pojo.xls.AbstractXlsEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Almat
 * created on 24/07/2020
 */

@Service("xlsx")
public class XlsxFileParser implements FileParser {

    @Override
    public <E extends AbstractXlsEntity> List<E> read(MultipartFile file, Class<E> entity) throws IOException {
        return null;
    }

    @Override
    public void write(MultipartFile file, Class<? extends AbstractXlsEntity> entity) {

    }

}
