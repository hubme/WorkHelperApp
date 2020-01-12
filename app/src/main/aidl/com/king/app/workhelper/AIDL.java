/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.king.app.workhelper;
//除了基本数据类型，其他类型的参数都需要标上方向类型：in(输入), out(输出), inout(输入输出)。

public interface AIDL extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements IRemoteService {
        private static final String DESCRIPTOR = "com.king.app.workhelper.IRemoteService";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.king.app.workhelper.IRemoteService
         * interface, generating a proxy if needed.
         */
        public static IRemoteService asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof IRemoteService))) {
                return ((IRemoteService) iin);
            }
            return new Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
                throws android.os.RemoteException {
            String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_add: {
                    data.enforceInterface(descriptor);
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
                    data.enforceInterface(descriptor);
                    java.util.List<com.king.app.workhelper.model.AidlModel> _result = this.getModels();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case TRANSACTION_registerModelListener: {
                    data.enforceInterface(descriptor);
                    IAidlModelListener _arg0;
                    _arg0 = IAidlModelListener.Stub.asInterface(data.readStrongBinder());
                    this.registerModelListener(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_unRegisterModelListener: {
                    data.enforceInterface(descriptor);
                    IAidlModelListener _arg0;
                    _arg0 = IAidlModelListener.Stub.asInterface(data.readStrongBinder());
                    this.unRegisterModelListener(_arg0);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements IRemoteService {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public void add(com.king.app.workhelper.model.AidlModel model) throws android.os.RemoteException {
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
                    mRemote.transact(Stub.TRANSACTION_add, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public java.util.List<com.king.app.workhelper.model.AidlModel> getModels()
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<com.king.app.workhelper.model.AidlModel> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_getModels, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArrayList(com.king.app.workhelper.model.AidlModel.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void registerModelListener(IAidlModelListener listener)
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
                    mRemote.transact(Stub.TRANSACTION_registerModelListener, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void unRegisterModelListener(IAidlModelListener listener)
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
                    mRemote.transact(Stub.TRANSACTION_unRegisterModelListener, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_getModels = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_registerModelListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_unRegisterModelListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    }

    public void add(com.king.app.workhelper.model.AidlModel model) throws android.os.RemoteException;

    public java.util.List<com.king.app.workhelper.model.AidlModel> getModels() throws android.os.RemoteException;

    public void registerModelListener(IAidlModelListener listener)
            throws android.os.RemoteException;

    public void unRegisterModelListener(IAidlModelListener listener)
            throws android.os.RemoteException;
}
