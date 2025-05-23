'use client';

import {useState} from "react";
import Paginator from "@/components/Paginator";

export type PagedData<T> = {
    content: T[] | null | undefined;
    empty: boolean;
    first: boolean;
    last: boolean;
    number: number;
    numberOfElements: number;
    size: number;
    totalElements: number;
    totalPages: number;
};

type TableProps<T> = {
    data: PagedData<T> | null | undefined;
    searchLabel?: string;
    onSearch?: (keyword: string) => void;
    onAdd?: () => void;
    onEdit?: (data: T) => void;
    onClickPage: (keyword: string, page: number) => void;
};

export default function Table<T>({data, searchLabel = "Keyword", onSearch, onAdd, onEdit, onClickPage}: TableProps<T>) {
    const keys: string[] = data?.content?.length ? Object.keys(data.content[0] as any) : [];
    const [keyword, setKeyword] = useState<string>("");

    return (
        <div className="overflow-hidden bg-white shadow-sm sm:rounded-lg">
            <div className="px-4 py-6 sm:px-6">
                {onSearch || onAdd
                    ? (<div className="mb-14 sm:flex sm:items-center">
                        <div className="sm:flex-auto">
                            <div className="relative">
                                {onSearch
                                    ? (<>
                                        <label htmlFor="keyword"
                                               className="absolute -top-2 left-2 inline-block bg-white px-1 text-xs font-medium text-neutral-900">
                                            {searchLabel}
                                        </label>
                                        <input value={keyword}
                                               onChange={({target: {value}}) => {
                                                   setKeyword(value);
                                               }}
                                               type="text"
                                               name="keyword"
                                               id="keyword"
                                               className="block w-full rounded-md border-0 px-3 py-1.5 text-neutral-900 shadow-xs ring-1 ring-inset ring-neutral-300 placeholder:text-neutral-400 focus:ring-2 focus:ring-inset focus:ring-primary-600 sm:text-sm sm:leading-6"
                                               placeholder="Forward matching keyword"/>
                                    </>)
                                    : <></>}
                            </div>
                        </div>
                        <div className="mt-4 sm:ml-4 sm:mt-0 sm:flex-none">
                            <div className={`relative`}>
                                {onSearch
                                    ? (<button
                                        onClick={() => {
                                            onSearch && onSearch(keyword)
                                        }}
                                        type="button"
                                        className="rounded-md bg-primary-600 px-3 py-2 text-center text-sm font-semibold text-white shadow-xs hover:bg-primary-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary-600">
                                        Search
                                    </button>)
                                    : <></>}
                                {onAdd
                                    ?
                                    <button
                                        onClick={onAdd}
                                        type="button"
                                        className="rounded-md bg-primary-600 ml-4 px-3 py-2 text-center text-sm font-semibold text-white shadow-xs hover:bg-primary-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary-600">
                                        Add
                                    </button>
                                    : <></>
                                }
                            </div>
                        </div>
                    </div>)
                    : <></>}
                <div className="-mt-6 flow-root">
                    <div className="-mx-4 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div className="inline-block min-w-full py-2 align-middle">
                            <table className="min-w-full divide-y divide-neutral-300">
                                <thead>
                                <tr>
                                    {keys.map((key, index) => (
                                        index === 0
                                            ? <th key={`${index}-${key}`}
                                                  scope="col"
                                                  className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-neutral-900 sm:pl-6 lg:pl-8">
                                                {key}
                                            </th>
                                            : <th key={`${index}-${key}`}
                                                  scope="col"
                                                  className="px-3 py-3.5 text-left text-sm font-semibold text-neutral-900">
                                                {key}
                                            </th>
                                    ))}
                                    {onEdit
                                        ? (<th scope="col" className="relative py-3.5 pl-3 pr-4 sm:pr-6 lg:pr-8">
                                            <span className="sr-only">Edit</span>
                                        </th>)
                                        : <></>
                                    }
                                </tr>
                                </thead>
                                <tbody className="divide-y divide-neutral-200 bg-white">
                                {data?.content?.map((item: any, rowIndex) => (
                                    <tr key={`${rowIndex}`}>
                                        {keys.map((key, colIndex) => (
                                            colIndex === 0
                                                ? <td key={`${rowIndex}-${colIndex}-${item[key]}`}
                                                      className="whitespace-nowrap overflow-hidden text-ellipsis max-w-xs py-4 pl-4 pr-3 text-sm font-medium text-neutral-900 sm:pl-6 lg:pl-8">
                                                    {item[key]}
                                                </td>
                                                : <td key={`${rowIndex}-${colIndex}-${item[key]}`}
                                                      className="whitespace-nowrap overflow-hidden text-ellipsis max-w-xs px-3 py-4 text-sm text-neutral-500">
                                                    {item[key]}
                                                </td>
                                        ))}
                                        {onEdit
                                            ? (
                                                <td className="relative whitespace-nowrap py-4 pl-3 pr-4 text-right text-sm font-medium sm:pr-6 lg:pr-8">
                                                    <button
                                                        onClick={() => {
                                                            onEdit(item);
                                                        }}
                                                        className="text-primary-600 hover:text-primary-900">
                                                        Edit<span className="sr-only">, {item.name}</span>
                                                    </button>
                                                </td>
                                            )
                                            : <></>
                                        }
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                {data && data.content
                    ? (<Paginator empty={data.empty}
                                  first={data.first}
                                  last={data.last}
                                  page={data.number}
                                  size={data.size}
                                  totalPages={data.totalPages}
                                  onClick={(page) => onClickPage(keyword, page)}/>)
                    : <></>
                }
            </div>
        </div>
    );
}