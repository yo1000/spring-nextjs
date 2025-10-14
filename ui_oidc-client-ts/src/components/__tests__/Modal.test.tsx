import {afterEach, describe, expect, it} from "@jest/globals";
import {act, fireEvent, render, screen} from "@testing-library/react";
import '@testing-library/jest-dom';
import Modal from "@/components/Modal";

describe(`Modal Component`, () => {
    const mockOnSave = jest.fn();
    const mockOnCancel = jest.fn();

    const defaultProps = {
        open: true,
        data: { name: `Squall Leonhart`, age: 17 },
        readonly: [`name`],
        title: `Test Modal`,
        onSave: mockOnSave,
        onCancel: mockOnCancel,
    };

    afterEach(() => {
        jest.clearAllMocks();
    });

    it(`should display the modal title when open`, async () => {
        await act(async () => {
            render(<Modal {...defaultProps} />);
        });

        expect(screen.getByText(`Test Modal`)).toBeInTheDocument();
    });

    it(`should display readonly fields as text`, async () => {
        await act(async () => {
            render(<Modal {...defaultProps} />);
        });

        expect(screen.getByText(`Squall Leonhart`)).toBeInTheDocument();
    });

    it(`should allow editing editable fields`, async () => {
        await act(async () => {
            render(<Modal {...defaultProps} />);
        });

        const ageInput: HTMLInputElement = screen.getByLabelText(`age`);
        fireEvent.change(ageInput, { target: { value: `20` } });

        expect(ageInput.value).toBe(`20`);
    });

    it(`should call onSave with updated data when Save button is clicked`, async () => {
        await act(async () => {
            render(<Modal {...defaultProps} />);
        });

        fireEvent.click(screen.getByText(`Save`));

        expect(mockOnSave).toHaveBeenCalledWith(defaultProps.data);
    });

    it(`should call onCancel when Cancel button is clicked`, async () => {
        await act(async () => {
            render(<Modal {...defaultProps} />);
        });

        fireEvent.click(screen.getByText(`Cancel`));

        expect(mockOnCancel).toHaveBeenCalledTimes(1);
    });

    it(`should not render modal content when open is false`, async () => {
        await act(async () => {
            render(<Modal {...defaultProps} open={false} />);
        });

        expect(screen.queryByText(`Test Modal`)).toBeNull();
    });
});
