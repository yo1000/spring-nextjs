import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import React from 'react';
import {describe, expect, test} from "@jest/globals";
import Content from "@/components/Content";

describe('Content Component', () => {
    test(`Text set in title property should be displayed.`, () => {
        // Arrange
        const title = 'Test Title';

        // Act
        render(<Content title={title}>Test Children</Content>);

        // Assert
        const titleElement = screen.getByRole('heading', { level: 1 });
        expect(titleElement).toHaveTextContent(title);
    });

    test(`Text set in children elements should be displayed.`, () => {
        // Arrange
        const childrenText = 'This is a child content';

        // Act
        render(<Content title="Sample Title">{childrenText}</Content>);

        // Assert
        const childrenElement = screen.getByText(childrenText);
        expect(childrenElement).toBeInTheDocument();
    });
});
