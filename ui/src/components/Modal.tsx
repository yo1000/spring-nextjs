'use client';

import {Dialog, DialogBackdrop, DialogPanel, DialogTitle} from '@headlessui/react'
import {useEffect, useState} from "react";

type OrderedName = {
    name: string;
    order: number;
};

export type NamedValue = {
    name: string;
    value: any | null | undefined;
    readonly: boolean | null | undefined;
};

type ModalProps = {
    open: boolean;
    data: any | null | undefined;
    readonly?: string[];
    title: string;
    saveLabel?: string;
    cancelLabel?: string;
    onSave: (data: any | null | undefined) => void;
    onCancel: () => void;
};

export default function Modal({open, data, readonly, title, saveLabel = "Save", cancelLabel = "Cancel", onSave, onCancel}: ModalProps) {
    const [editData, setEditData] = useState<NamedValue[] | null | undefined>();
    const [orders, setOrders] = useState<OrderedName[] | null | undefined>();

    useEffect(() => {
        if (open) {
            setEditData(Object.entries(data)
                .map(([key, value]) => ({
                    name: key,
                    value: value,
                    readonly: readonly?.includes(key),
                }))
            );

            setOrders(Object.entries(data)
                .map(([key, _value], index) => ({
                    name: key,
                    order: index,
                }))
            );
        }
    }, [open]);

    return (
        <Dialog className="relative z-10" open={open} onClose={() => {}}>
            <DialogBackdrop
                transition
                className="fixed inset-0 bg-neutral-500 bg-opacity-75 transition-opacity data-[closed]:opacity-0 data-[enter]:duration-300 data-[leave]:duration-200 data-[enter]:ease-out data-[leave]:ease-in"/>

            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-end justify-center p-4 text-center items-center sm:items-center sm:p-0">
                    <DialogPanel
                        transition
                        className="relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all data-[closed]:translate-y-4 data-[closed]:opacity-0 data-[enter]:duration-300 data-[leave]:duration-200 data-[enter]:ease-out data-[leave]:ease-in sm:my-8 w-full sm:w-full sm:max-w-3xl sm:p-6 data-[closed]:sm:translate-y-0 data-[closed]:sm:scale-95">
                        <div className="sm:items-start">
                            <div className="mt-3 text-center sm:mt-0 sm:text-left">
                                <DialogTitle as="h3" className="text-base font-semibold leading-6 text-neutral-900">
                                    {title}
                                </DialogTitle>
                                <div className="mt-2">
                                    <div className="mt-6 grid grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-6">
                                        {editData?.map((datum, index) => (
                                            <div key={`modal-${title}-${index}-${datum.name}`} className="sm:col-span-3">
                                                <label htmlFor={`modal-${title}-${index}-${datum.name}`}
                                                       className="block text-sm font-medium leading-6 text-neutral-900">
                                                    {datum.name}
                                                </label>
                                                <div className="mt-2">
                                                    {datum.readonly
                                                        ? <span>{datum.value}</span>
                                                        : <input
                                                            type="text"
                                                            name={datum.name}
                                                            id={`modal-${title}-${index}-${datum.name}`}
                                                            value={datum.value}
                                                            onChange={({target: {value}}) => {
                                                                setEditData([
                                                                    ...(editData?.filter(d => (d.name !== datum.name)) ?? []),
                                                                    {
                                                                        name: datum.name,
                                                                        value: value,
                                                                        readonly: datum.readonly,
                                                                    }
                                                                ].sort((a, b) => {
                                                                    const aa = orders?.find(o => o.name === a.name);
                                                                    const bb = orders?.find(o => o.name === b.name);

                                                                    if (!aa && !bb) return 0;
                                                                    if (!aa) return -1;
                                                                    if (!bb) return 1;

                                                                    return aa.order - bb.order;
                                                                }));
                                                            }}
                                                            className="block w-full rounded-md border-0 py-1.5 px-2 text-neutral-900 shadow-sm ring-1 ring-inset ring-neutral-300 placeholder:text-neutral-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                                                        />}
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="mt-6 sm:mt-6 sm:flex sm:flex-row-reverse">
                            <button
                                type="button"
                                className="inline-flex w-full justify-center rounded-md bg-primary-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-primary-500 sm:ml-3 sm:w-auto"
                                onClick={() => {
                                    const saveData: any = {};

                                    for (const editDatum of (editData ?? [])) {
                                        saveData[editDatum.name] = editDatum.value;
                                    }

                                    onSave(saveData);
                                }}>
                                {saveLabel}
                            </button>
                            <button
                                type="button"
                                className="mt-3 inline-flex w-full justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-neutral-900 shadow-sm ring-1 ring-inset ring-neutral-300 hover:bg-neutral-50 sm:mt-0 sm:w-auto"
                                onClick={onCancel}
                                data-autofocus>
                                {cancelLabel}
                            </button>
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    )
}
