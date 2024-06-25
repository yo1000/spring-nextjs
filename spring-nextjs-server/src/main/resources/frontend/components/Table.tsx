import {useState} from "react";

type PagedData = {
    content: any[] | null | undefined;
};

type TableProps = {
    data: PagedData | null | undefined;
    searchLabel?: string;
    onSearch: (keyword: string) => void;
    onAdd?: () => void;
    onEdit?: (data: any) => void;
};

export default function Table({data, searchLabel = "Keyword", onSearch, onAdd, onEdit}: TableProps) {
    const keys: string[] = data?.content?.length ? Object.keys(data.content[0]) : [];
    const [keyword, setKeyword] = useState<string>("");

    return (
        <div className="overflow-hidden bg-white shadow sm:rounded-lg">
            <div className="px-4 py-6 sm:px-6">
                <div className="sm:flex sm:items-center">
                    <div className="sm:flex-auto">
                        <div className="relative">
                            <label htmlFor="keyword"
                                   className="absolute -top-2 left-2 inline-block bg-white px-1 text-xs font-medium text-gray-900">
                                {searchLabel}
                            </label>
                            <input value={keyword}
                                   onChange={({target: {value}}) => {
                                       setKeyword(value);
                                   }}
                                   type="text"
                                   name="keyword"
                                   id="keyword"
                                   className="block w-full rounded-md border-0 px-2 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                   placeholder="Keyword"/>
                        </div>
                    </div>
                    <div className="mt-4 sm:ml-4 sm:mt-0 sm:flex-none">
                        <div className={`relative`}>
                            <button
                                onClick={() => {
                                    onSearch && onSearch(keyword)
                                }}
                                type="button"
                                className="rounded-md bg-indigo-600 px-3 py-2 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                                Search
                            </button>
                            {onAdd
                                ?
                                <button
                                    onClick={onAdd}
                                    type="button"
                                    className="rounded-md bg-indigo-600 ml-4 px-3 py-2 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                                    Add
                                </button>
                                : <></>
                            }

                        </div>
                    </div>
                </div>
                <div className="mt-8 flow-root">
                    <div className="-mx-4 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div className="inline-block min-w-full py-2 align-middle">
                            <table className="min-w-full divide-y divide-gray-300">
                                <thead>
                                <tr>
                                    {keys.map((key, index) => (
                                        index === 0
                                            ? <th key={`${index}-${key}`}
                                                  scope="col"
                                                  className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6 lg:pl-8">
                                                {key}
                                            </th>
                                            : <th key={`${index}-${key}`}
                                                  scope="col"
                                                  className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
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
                                <tbody className="divide-y divide-gray-200 bg-white">
                                {data?.content?.map((item) => (
                                    <tr key={item.id}>
                                        {keys.map((key, index) => (
                                            index === 0
                                                ? <td key={`${index}-${item[key]}`}
                                                      className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6 lg:pl-8">
                                                    {item[key]}
                                                </td>
                                                : <td key={`${index}-${item[key]}`}
                                                      className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
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
                                                        className="text-indigo-600 hover:text-indigo-900">
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
            </div>
        </div>
    );
}