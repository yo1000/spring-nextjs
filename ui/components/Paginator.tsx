import {ArrowLongLeftIcon, ArrowLongRightIcon} from '@heroicons/react/20/solid'

type PaginatorProps = {
    empty: boolean;
    first: boolean;
    last: boolean;
    page: number;
    size: number;
    totalPages: number;
    onClick: (page: number) => void;
};

export default function Paginator({empty, first, last, page, size, totalPages, onClick}: PaginatorProps) {
    const initArray = (from: number, to: number) => {
        const a = new Array<number>();
        for (let i = from; i <= to; i++) {
            a.push(i);
        }
        return a;
    };

    const createPages = (from: number, to: number, page: number, totalPages: number) => {
        const firstArray = initArray(0, 2);
        const middleArray = initArray(Math.max(0, page - 2), Math.min(page + 2, totalPages - 1));
        const lastArray = initArray(totalPages - 3, totalPages - 1);

        const mergedArray = Array.from(new Set([...firstArray, ...middleArray, ...lastArray]));
        const pageArray = new Array<number | null>();

        for (let i = 0; i < mergedArray.length; i++) {
            const el = mergedArray[i];
            if (i > 0 && el - 1 !== mergedArray[i - 1]) {
                pageArray.push(null);
            }
            pageArray.push(el);
        }

        return pageArray;
    };

    const pages = (totalPages < 12
        ? initArray(0, totalPages - 1)
        : createPages(0, totalPages - 1, page, totalPages))
        .map(el => (el || el === 0
            ? {
                page: el,
                pageLabel: el + 1,
                current: el === page
            }
            : null));

    return (
        <nav className="flex items-center justify-between border-t border-gray-200 overflow-x-auto px-4 sm:px-0 -mx-4 sm:-mx-6 lg:-mx-8">
            <div className="flex flex-1 overflow-x-auto -mx4 sm:mx-6 lg:mx-8">
                <div className="-mt-px flex w-0 flex-1">
                    {first
                        ? <></>
                        : (
                            <button className="inline-flex items-center border-t-2 border-transparent pr-1 pt-4 text-sm font-medium text-gray-500 hover:border-gray-300 hover:text-gray-700"
                                onClick={() => {
                                    onClick(page - 1);
                                }}>
                                <ArrowLongLeftIcon className="mr-3 h-5 w-5 text-gray-400" aria-hidden="true"/>
                                Previous
                            </button>
                        )
                    }
                </div>
                <div className="hidden md:-mt-px md:flex">
                    {pages.map((p, index) => (
                        p ? (
                            <button key={`page-${index}-${p.page}`} className={p.current
                                ? `inline-flex items-center border-t-2 border-indigo-500 px-4 pt-4 text-sm font-medium text-indigo-600`
                                : `inline-flex items-center border-t-2 border-transparent px-4 pt-4 text-sm font-medium text-gray-500 hover:border-gray-300 hover:text-gray-700`}
                                    onClick={() => {
                                        onClick(p!.page);
                                    }}>
                                {p.pageLabel}
                            </button>
                        ) : (
                            <span key={`page-${index}`} className="inline-flex items-center border-t-2 border-transparent px-4 pt-4 text-sm font-medium text-gray-500">
                                ...
                            </span>
                        )
                    ))}
                </div>
                <div className="-mt-px flex w-0 flex-1 justify-end">
                    {last
                        ? <></>
                        : (
                            <button className="inline-flex items-center border-t-2 border-transparent pl-1 pt-4 text-sm font-medium text-gray-500 hover:border-gray-300 hover:text-gray-700"
                                    onClick={() => {
                                        onClick(page + 1);
                                    }}>
                                Next
                                <ArrowLongRightIcon className="ml-3 h-5 w-5 text-gray-400" aria-hidden="true"/>
                            </button>
                        )
                    }
                </div>
            </div>
        </nav>
    )
}
