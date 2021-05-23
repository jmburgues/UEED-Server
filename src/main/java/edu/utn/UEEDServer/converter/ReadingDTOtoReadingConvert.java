package edu.utn.UEEDServer.converter;

import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.dto.ReadingDTO;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReadingDTOtoReadingConvert implements Converter <ReadingDTO, Reading>{

    private ModelMapper modelMapper;

    public ReadingDTOtoReadingConvert(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public Reading convert(ReadingDTO source){
        return modelMapper.map(source,Reading.class);
    }
}
