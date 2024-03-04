package opekope2.optigui.filter

import opekope2.optigui.filter.IFilter.Result.Match

/**
 * A post-processor filter, which enables the output of the given sub-filter to be changed.
 *
 * Useful, when the sub-filter doesn't yield a result (like [ConjunctionFilter] or [DisjunctionFilter])
 *
 * @param T The type the filter accepts
 * @param TFilterResult The type the sub-filter's returns
 * @param TResult The type the filter returns
 * @param filter The sub-filter to evaluate
 * @param transformDescription Textual description of [transform] for better [dump] readability
 * @param transform The function, which transforms the result of [evaluate].
 * Its input is both the input of [evaluate] and the result of [filter]
 *
 * @see PreProcessorFilter
 */
class PostProcessorFilter<T, TFilterResult, TResult>(
    private val filter: IFilter<T, out TFilterResult>,
    private val transformDescription: String,
    private val transform: (input: T, result: IFilter.Result<out TFilterResult>) -> IFilter.Result<out TResult>
) : IFilter<T, TResult>, Iterable<IFilter<T, out TFilterResult>> {
    /**
     * Creates a new post-processor filter by specifying [Match.result].
     *
     * @param filter The sub-filter to evaluate
     * @param result The (constant) result of the [transform] function
     */
    constructor(filter: IFilter<T, out TFilterResult>, result: TResult) : this(
        filter,
        "Override match result with `$result`",
        { _, filterResult -> filterResult.withResult(result) }
    )

    override fun evaluate(value: T): IFilter.Result<out TResult> = transform(value, filter.evaluate(value))

    override fun iterator(): Iterator<IFilter<T, out TFilterResult>> = setOf(filter).iterator()

    override fun toString(): String = "${javaClass.name}, transform: $transformDescription"
}