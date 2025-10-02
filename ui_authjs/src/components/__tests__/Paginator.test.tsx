import {describe, expect, test} from "@jest/globals";
import {fireEvent, render, screen} from "@testing-library/react";
import '@testing-library/jest-dom';
import Paginator from "@/components/Paginator";

describe(`Paginator Component`, () => {
    test(`When settings for 1-page are set in Page and TotalPage props, navigation for 1-page should be displayed.`, () => {
        render(<Paginator
            empty={false}
            first={true} last={true}
            page={0} size={10} totalPages={1}
            onClick={() => {}}/>);

        expect(screen.queryByText(`1`)).toBeDefined();
        expect(screen.queryByText(`Previous`)).toBeNull();
        expect(screen.queryByText(`Next`)).toBeNull();
        expect(screen.queryByText(`2`)).toBeNull();
    });

    test(`When settings for 2-page are set in Page and TotalPage props, navigation for 2-page should be displayed.`, () => {
        render(<Paginator
            empty={false}
            first={true} last={false}
            page={0} size={10} totalPages={2}
            onClick={() => {}}/>);

        expect(screen.queryByText(`1`)).toBeInTheDocument();
        expect(screen.queryByText(`2`)).toBeInTheDocument();
        expect(screen.queryByText(`Previous`)).toBeNull();
        expect(screen.queryByText(`Next`)).toBeInTheDocument();
        expect(screen.queryByText(`3`)).toBeNull();
    });

    test(`When there are many pages and middle of whole page is active, ellipsis should be displayed.`, () => {
        render(
            <Paginator
                empty={false}
                first={false}
                last={false}
                page={10}
                size={10}
                totalPages={20}
                onClick={() => {}}
            />
        );

        expect(screen.getAllByText(`...`)).toHaveLength(2);
    });

    test(`When click page number navigation, should be call click handler with choose page number -1.`, () => {
        const onClickMock = jest.fn();

        render(
            <Paginator
                empty={false}
                first={false}
                last={false}
                page={2}
                size={10}
                totalPages={5}
                onClick={onClickMock}
            />
        );

        const clickNumber = 4;

        const pageButton = screen.getByText(`${clickNumber}`);
        fireEvent.click(pageButton);

        expect(onClickMock).toHaveBeenCalledWith(clickNumber - 1);
    });

    test(`When click Next page navigation, should be call click handler with active page number + 1.`, () => {
        const onClickMock = jest.fn();

        const activePage = 2

        render(
            <Paginator
                empty={false}
                first={false}
                last={false}
                page={activePage}
                size={10}
                totalPages={5}
                onClick={onClickMock}
            />
        );

        const pageButton = screen.getByText(`Next`);
        fireEvent.click(pageButton);

        expect(onClickMock).toHaveBeenCalledWith(activePage + 1);
    });


    test(`When click Previous page navigation, should be call click handler with active page number - 1.`, () => {
        const onClickMock = jest.fn();

        const activePage = 2

        render(
            <Paginator
                empty={false}
                first={false}
                last={false}
                page={activePage}
                size={10}
                totalPages={5}
                onClick={onClickMock}
            />
        );

        const pageButton = screen.getByText(`Previous`);
        fireEvent.click(pageButton);

        expect(onClickMock).toHaveBeenCalledWith(activePage - 1);
    });
});
