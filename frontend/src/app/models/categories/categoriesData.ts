export interface CategoriesData {
    categoryAndParents: string[];
    childrenCategories: CategoriesData[];
}