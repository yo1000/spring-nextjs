export default function CheckSvg() {
    return (
        <svg
            fill="none" viewBox="0 0 14 14"
            className="pointer-events-none col-start-1 row-start-1 size-3.5 self-center justify-self-center stroke-white group-has-[:disabled]:stroke-gray-950/25">
            <path
                d="M3 8L6 11L11 3.5" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round"
                className="opacity-0 group-has-[:checked]:opacity-100"/>
            <path d="M3 7H11" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round"
                  className="opacity-0 group-has-[:indeterminate]:opacity-100"/>
        </svg>
    );
}
