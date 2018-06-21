/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Android\\WorkHelperApp\\app\\src\\main\\aidl\\com\\king\\app\\workhelper\\IRemoteService.aidl
 */
package com.king.app.workhelper.service;
//除了基本数据类型，其他类型的参数都需要标上方向类型：in(输入), out(输出), inout(输入输出)。

/**
 * 文件 copy，进行了格式化，方便阅读。
 *
 * @author VanceKing
 * @since 2018/6/21.
 */
public interface IRemoteService extends android.os.IInterface {
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements com.king.app.workhelper.IRemoteService {
        private static final java.lang.String DESCRIPTOR = "com.king.app.workhelper.IRemoteService";

        /** Construct the stub at attach it to the interface. */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.king.app.workhelper.IRemoteService interface,
         * generating a proxy if needed.
         */
        public static com.king.app.workhelper.IRemoteService asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.king.app.workhelper.IRemoteService))) {
                return ((com.king.app.workhelper.IRemoteService) iin);
            }
            return new Proxy(obj);
        }

        @Override public android.os.IBinder asBinder() {
            return this;
        }

        @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_add: {
                    data.enforceInterface(DESCRIPTOR);
                    com.king.app.workhelper.model.AidlModel _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = com.king.app.workhelper.model.AidlModel.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.add(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_getModels: {
                    data.enforceInterface(DESCRIPTOR);
                    java.util.List<com.king.app.workhelper.model.AidlModel> _result = this.getModels();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case TRANSACTION_getPid: {
                    data.enforceInterface(DESCRIPTOR);
                    int _result = this.getPid();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case TRANSACTION_getPName: {
                    data.enforceInterface(DESCRIPTOR);
                    java.lang.String _result = this.getPName();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements com.king.app.workhelper.IRemoteService {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override public void add(com.king.app.workhelper.model.AidlModel model) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((model != null)) {
                        _data.writeInt(1);
                        model.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(TRANSACTION_add, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override public java.util.List<com.king.app.workhelper.model.AidlModel> getModels() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<com.king.app.workhelper.model.AidlModel> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(TRANSACTION_getModels, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArrayList(com.king.app.workhelper.model.AidlModel.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override public int getPid() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(TRANSACTION_getPid, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override public java.lang.String getPName() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(TRANSACTION_getPName, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }

        static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_getModels = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_getPid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_getPName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    }

    public void add(com.king.app.workhelper.model.AidlModel model) throws android.os.RemoteException;

    public java.util.List<com.king.app.workhelper.model.AidlModel> getModels() throws android.os.RemoteException;

    public int getPid() throws android.os.RemoteException;

    public java.lang.String getPName() throws android.os.RemoteException;
}
