package com.siseth.adapter.dto.camera.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FilesCameraResDTO {

    private List<String> added;

    private List<String> rejected;

    private List<String> omitted;

}
