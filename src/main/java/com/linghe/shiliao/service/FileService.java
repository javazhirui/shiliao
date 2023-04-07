package com.linghe.shiliao.service;

import com.linghe.shiliao.common.R;

import java.io.File;

public abstract class FileService {
    public abstract R<String> upload(File file);
}
