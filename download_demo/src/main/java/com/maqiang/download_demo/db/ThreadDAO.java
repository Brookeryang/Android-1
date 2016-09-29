package com.maqiang.download_demo.db;

import com.maqiang.download_demo.entities.ThreadInfo;

import java.util.List;

/**
 * Created by maqiang on 16/9/12.
 * 数据访问接口
 */
public interface ThreadDAO {
    /**
     * 插入线程信息
     * @param threadInfo
     */
    void insertThread(ThreadInfo threadInfo);

    /**
     * 删除线程信息
     * @param url
     * @param thread_id
     */
    void deleteThread(String url,int thread_id);

    /**
     * 更新线程下载进度
     * @param url
     * @param thread_id
     * @param finished
     */
    void updateTHread(String url,int thread_id,int finished);

    /**
     * 查询文件的线程信息
     * @param url
     * @return
     */
    List<ThreadInfo> getThreads(String url);

    /**
     * 判断线程信息是否已经存在
     * @param url
     * @param thread_id
     * @return
     */
    boolean isExists(String url,int thread_id);
}
