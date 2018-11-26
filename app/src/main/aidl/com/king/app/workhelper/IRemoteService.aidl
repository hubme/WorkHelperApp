// IRemoteService.aidl
package com.king.app.workhelper;

//必须要导入
import com.king.app.workhelper.model.AidlModel;
import com.king.app.workhelper.IAidlModelListener;

//除了基本数据类型，其他类型的参数都需要标上方向类型：in(输入), out(输出), inout(输入输出)。
interface IRemoteService {
    void add(in AidlModel model);
    List<AidlModel> getModels();
    int getPid();
    String getPName();
    void registerModelListener(IAidlModelListener listener);
    void unRegisterModelListener(IAidlModelListener listener);
}
