'use client';

import {Dialog, DialogBackdrop, DialogPanel, DialogTitle} from '@headlessui/react'
import {useEffect, useState} from "react";
import CheckSvg from "@/components/svgs/CheckSvg";

export enum ComponentType {
    Text,
    TextArea,
    Select,
    Checkbox,
    Label,
}

type Option = {
    label: string;
    value: any;
};

type DataConfig = {
    name: string;
    index?: number;
    colspan?: number;
    type: ComponentType;
    options?: Option[];
};

type Button = {
    label?: string;
    onClick: (data?: any) => void;
}

type ModalProps = {
    open: boolean;
    title: string;
    data: any | undefined;
    dataConfig?: DataConfig[];
    save: Button;
    cancel: Button;
};

type EditData = {
    config: DataConfig,
    value: any | undefined,
}

export default function Modal({open, title, data, dataConfig, save, cancel}: ModalProps) {
    const [editData, setEditData] = useState<EditData[] | null | undefined>();
    const saveButton = {
        label: save.label ?? `Save`,
        onClick: save.onClick
    } as Button;
    const cancelButton = {
        label: cancel.label ?? `Cancel`,
        onClick: cancel.onClick
    } as Button;

    useEffect(() => {
        if (open) {
            setEditData(Object.entries(data)
                .map(([key, value], index) => {
                    const config = dataConfig?.find(c => c.name === key);
                    return {
                        config: {
                            name: key,
                            index: index,
                            colspan: config?.colspan ?? 1,
                            type: config?.type ?? ComponentType.Text,
                            options: config?.options ?? [],
                        },
                        value: value,
                    };
                })
            )
        }
    }, [open, data, dataConfig]);

    return (
        <Dialog className="relative z-10" open={open} onClose={() => {}}>
            <DialogBackdrop
                transition
                className="fixed inset-0 bg-gray-500/75 transition-opacity data-[closed]:opacity-0 data-[enter]:duration-300 data-[leave]:duration-200 data-[enter]:ease-out data-[leave]:ease-in"/>

            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-end justify-center p-4 text-center items-center sm:items-center sm:p-0">
                    <DialogPanel
                        transition
                        className="relative transform overflow-hidden rounded-lg bg-white pb-4 pt-2 sm:pt-5 text-left shadow-xl transition-all data-closed:translate-y-4 data-closed:opacity-0 data-enter:duration-300 data-leave:duration-200 data-enter:ease-out data-leave:ease-in sm:my-8 w-full sm:w-full sm:max-w-3xl sm:data-closed:translate-y-0 sm:data-closed:scale-95">
                        <div className="sm:items-start">
                            <div className="mt-3 text-center sm:mt-0 sm:text-left">
                                <div className="border-b border-gray-200 pb-5">
                                <DialogTitle as="h3" className="text-base font-semibold leading-6 text-neutral-900 px-4 sm:px-6">
                                    {title}
                                </DialogTitle>
                                </div>
                                <div className="mt-2 px-4 sm:px-6">
                                    <div className="mt-4 sm:mt-6 grid grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-2 text-left">
                                        {editData?.map((datum, index) => (
                                            <div key={`modal-${title}-${index}-${datum.config.name}`} className={`sm:col-span-${datum.config.colspan}`}>
                                                <label htmlFor={`modal-${title}-${index}-${datum.config.name}`}
                                                       className="block text-sm font-medium leading-6 text-neutral-900">
                                                    {datum.config.name}
                                                </label>
                                                <div className="mt-2">
                                                    {
                                                        datum.config.type === ComponentType.Label ? <span>{datum.value}</span>
                                                        : datum.config.type === ComponentType.Text ? <input
                                                                className={`block w-full h-9 rounded-md border-0 py-1.5 px-2 text-neutral-900 shadow-xs ring-1 ring-inset ring-neutral-300 placeholder:text-neutral-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6`}
                                                                type={`text`}
                                                                name={datum.config.name}
                                                                id={`modal-${title}-${index}-${datum.config.name}`}
                                                                value={datum.value}
                                                                onChange={({target: {value}}) => {
                                                                    setEditData([
                                                                        ...(editData?.filter(d => d.config.name !== datum.config.name) ?? []),
                                                                        {
                                                                            ...datum,
                                                                            value: value,
                                                                        }
                                                                    ].sort((a, b) => {
                                                                        if (!a.config.index && !b.config.index) return 0;
                                                                        if (!a.config.index) return -1;
                                                                        if (!b.config.index) return 1;
                                                                        return a.config.index - b.config.index;
                                                                    }));
                                                                }}/>
                                                        : datum.config.type === ComponentType.TextArea ? <textarea
                                                                className={`block w-full rounded-md border-0 py-1.5 px-2 text-neutral-900 shadow-xs ring-1 ring-inset ring-neutral-300 placeholder:text-neutral-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6`}
                                                                name={datum.config.name}
                                                                id={`modal-${title}-${index}-${datum.config.name}`}
                                                                onChange={({target: {value}}) => {
                                                                    setEditData([
                                                                        ...(editData?.filter(d => d.config.name !== datum.config.name) ?? []),
                                                                        {
                                                                            ...datum,
                                                                            value: value,
                                                                        }
                                                                    ].sort((a, b) => {
                                                                        if (!a.config.index && !b.config.index) return 0;
                                                                        if (!a.config.index) return -1;
                                                                        if (!b.config.index) return 1;
                                                                        return a.config.index - b.config.index;
                                                                    }));
                                                                }}>{datum.value}</textarea>
                                                        : datum.config.type === ComponentType.Select ? <select
                                                                className={`block w-full h-9 rounded-md border-0 py-1.5 px-2 text-neutral-900 shadow-xs ring-1 ring-inset ring-neutral-300 placeholder:text-neutral-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6`}
                                                                name={datum.config.name}
                                                                id={`modal-${title}-${index}-${datum.config.name}`}
                                                                onChange={({target: {value}}) => {
                                                                    setEditData([
                                                                        ...(editData?.filter(d => d.config.name !== datum.config.name) ?? []),
                                                                        {
                                                                            ...datum,
                                                                            value: value,
                                                                        }
                                                                    ].sort((a, b) => {
                                                                        if (!a.config.index && !b.config.index) return 0;
                                                                        if (!a.config.index) return -1;
                                                                        if (!b.config.index) return 1;
                                                                        return a.config.index - b.config.index;
                                                                    }));
                                                                }}>{datum.config.options?.map(o => (<option
                                                                            key={o.value}
                                                                            value={o.value}
                                                                            selected={o.value === datum.value}>{o.label}</option>))}</select>
                                                        : datum.config.type === ComponentType.Checkbox ? (datum.config.options?.map(o => (<div key={o.value} className="flex gap-3"><div className="flex h-6 shrink-0 items-center"><div className="group grid size-4 grid-cols-1"><input
                                                                className={`col-start-1 row-start-1 appearance-none rounded-sm border border-gray-300 bg-white checked:border-primary-600 checked:bg-primary-600 indeterminate:border-primary-600 indeterminate:bg-primary-600 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary-600 disabled:border-gray-300 disabled:bg-gray-100 disabled:checked:bg-gray-100 forced-colors:appearance-auto`}
                                                                type={`checkbox`}
                                                                name={datum.config.name}
                                                                id={`modal-${title}-${index}-${datum.config.name}-${o.value}`}
                                                                value={o.value}
                                                                checked={Array.isArray(datum.value) ? (datum.value as any[]).includes(o.value) : o.value === datum.value}
                                                                onChange={({target: {value, checked}}) => {
                                                                    const values: any[] = (Array.isArray(datum.value) ? datum.value : [datum.value]);
                                                                    const changedValues = checked
                                                                        ? [...values, value]
                                                                        : values.filter(v => v !== value);

                                                                    setEditData([
                                                                        ...(editData?.filter(d => d.config.name !== datum.config.name) ?? []),
                                                                        {
                                                                            ...datum,
                                                                            value: changedValues,
                                                                        }
                                                                    ].sort((a, b) => {
                                                                        if (!a.config.index && !b.config.index) return 0;
                                                                        if (!a.config.index) return -1;
                                                                        if (!b.config.index) return 1;
                                                                        return a.config.index - b.config.index;
                                                                    }));
                                                                }}/><CheckSvg/></div></div><div className="text-sm/6"><label
                                                                htmlFor={`modal-${title}-${index}-${datum.config.name}-${o.value}`} className="font-medium text-gray-900">{o.label}</label></div></div>)))
                                                        : <></>
                                                    }
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="mt-6 sm:mt-6 sm:flex sm:flex-row-reverse px-4 sm:px-6 pb-1">
                            <button
                                type="button"
                                className="inline-flex w-full justify-center rounded-md bg-primary-600 px-3 py-2 text-sm font-semibold text-white shadow-xs hover:bg-primary-500 sm:ml-3 sm:w-auto"
                                onClick={() => {
                                    const saveData: any = {};

                                    for (const editDatum of (editData ?? [])) {
                                        saveData[editDatum.config.name] = editDatum.value;
                                    }

                                    saveButton.onClick(saveData);
                                }}>
                                {saveButton.label}
                            </button>
                            <button
                                type="button"
                                className="mt-3 inline-flex w-full justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-neutral-900 shadow-xs ring-1 ring-inset ring-neutral-300 hover:bg-neutral-50 sm:mt-0 sm:w-auto"
                                onClick={cancelButton.onClick}
                                data-autofocus={`data-autofocus`}>
                                {cancelButton.label}
                            </button>
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    )
}
